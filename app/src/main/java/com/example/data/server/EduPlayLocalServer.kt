package com.example.data.server

import android.content.Context
import com.example.data.local.EduPlayDatabase
import com.example.data.local.UserEntity
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class ActionRequest(val username: String, val xpDelta: Int, val gemsDelta: Int, val lessonDelta: Int, val gameDelta: Int)

@Serializable
data class UserResponse(
    val username: String,
    val name: String,
    val fullName: String,
    val handle: String,
    val dayStreak: Int,
    val totalXp: Int,
    val gemsCount: Int,
    val lessonsCompleted: Int,
    val gamesPlayed: Int,
    val livesCount: Int
)

class EduPlayLocalServer(private val context: Context) {
    private var server: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

    fun start() {
        if (server != null) return
        val userDao = EduPlayDatabase.getDatabase(context).userDao()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                server = embeddedServer(CIO, port = 8080) {
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                        })
                    }
                    routing {
                        post("/api/login") {
                            try {
                                val req = call.receive<LoginRequest>()
                                val trimmed = req.username.trim().lowercase()
                                val now = System.currentTimeMillis()
                                val twentyFourHours = 24 * 60 * 60 * 1000L

                                val existing = userDao.getUser(trimmed)
                                userDao.clearCurrentUsers()

                                val userEntity = if (existing != null) {
                                    val shouldReset = (now - existing.lastResetTimestamp) > twentyFourHours
                                    val updated = if (shouldReset) {
                                        existing.copy(
                                            livesCount = 10,
                                            lessonsCompleted = 0,
                                            gamesPlayed = 0,
                                            lastResetTimestamp = now,
                                            isCurrent = true
                                        )
                                    } else {
                                        existing.copy(isCurrent = true)
                                    }
                                    userDao.updateUser(updated)
                                    updated
                                } else {
                                    val newUser = UserEntity(
                                        username = trimmed,
                                        password = req.password,
                                        name = trimmed.replaceFirstChar { it.uppercase() },
                                        fullName = trimmed.replaceFirstChar { it.uppercase() + " Estudiante" },
                                        handle = "@$trimmed",
                                        dayStreak = 1,
                                        totalXp = 50,
                                        gemsCount = 15,
                                        lessonsCompleted = 0,
                                        gamesPlayed = 0,
                                        livesCount = 10,
                                        lastResetTimestamp = now,
                                        isCurrent = true
                                    )
                                    userDao.insertUser(newUser)
                                    newUser
                                }

                                call.respond(
                                    UserResponse(
                                        username = userEntity.username,
                                        name = userEntity.name,
                                        fullName = userEntity.fullName,
                                        handle = userEntity.handle,
                                        dayStreak = userEntity.dayStreak,
                                        totalXp = userEntity.totalXp,
                                        gemsCount = userEntity.gemsCount,
                                        lessonsCompleted = userEntity.lessonsCompleted,
                                        gamesPlayed = userEntity.gamesPlayed,
                                        livesCount = userEntity.livesCount
                                    )
                                )
                            } catch (e: Exception) {
                                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage ?: "Error")
                            }
                        }

                        post("/api/action") {
                            try {
                                val req = call.receive<ActionRequest>()
                                val user = userDao.getUser(req.username.trim().lowercase())
                                if (user != null) {
                                    val now = System.currentTimeMillis()
                                    val twentyFourHours = 24 * 60 * 60 * 1000L
                                    val shouldReset = (now - user.lastResetTimestamp) > twentyFourHours

                                    val baseLives = if (shouldReset) 10 else user.livesCount
                                    val newLives = (baseLives - 1).coerceAtLeast(0)

                                    val updated = user.copy(
                                        totalXp = if (shouldReset) req.xpDelta else user.totalXp + req.xpDelta,
                                        gemsCount = if (shouldReset) req.gemsDelta else user.gemsCount + req.gemsDelta,
                                        lessonsCompleted = if (shouldReset) req.lessonDelta else user.lessonsCompleted + req.lessonDelta,
                                        gamesPlayed = if (shouldReset) req.gameDelta else user.gamesPlayed + req.gameDelta,
                                        livesCount = newLives,
                                        lastResetTimestamp = if (shouldReset) now else user.lastResetTimestamp
                                    )
                                    userDao.updateUser(updated)

                                    call.respond(
                                        UserResponse(
                                            username = updated.username,
                                            name = updated.name,
                                            fullName = updated.fullName,
                                            handle = updated.handle,
                                            dayStreak = updated.dayStreak,
                                            totalXp = updated.totalXp,
                                            gemsCount = updated.gemsCount,
                                            lessonsCompleted = updated.lessonsCompleted,
                                            gamesPlayed = updated.gamesPlayed,
                                            livesCount = updated.livesCount
                                        )
                                    )
                                } else {
                                    call.respond(HttpStatusCode.NotFound, "User not found")
                                }
                            } catch (e: Exception) {
                                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage ?: "Error")
                            }
                        }
                    }
                }.start(wait = false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stop() {
        try {
            server?.stop(500, 1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        server = null
    }
}
