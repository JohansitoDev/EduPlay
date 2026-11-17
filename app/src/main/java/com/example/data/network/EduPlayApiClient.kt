package com.example.data.network

import android.content.Context
import com.example.data.local.EduPlayDatabase
import com.example.data.local.UserEntity
import com.example.data.server.UserResponse

class EduPlayApiClient(context: Context) {
    private val userDao = EduPlayDatabase.getDatabase(context).userDao()

    suspend fun login(username: String, password: String): UserResponse? {
        val rawName = if (username.isBlank()) "Invitado" else username
        val trimmed = rawName.trim().lowercase()
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
                password = password,
                name = rawName.replaceFirstChar { it.uppercase() },
                fullName = rawName.replaceFirstChar { it.uppercase() + " Estudiante" },
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

        return UserResponse(
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
    }

    suspend fun recordAction(username: String, xpDelta: Int, gemsDelta: Int, lessonDelta: Int, gameDelta: Int): UserResponse? {
        val user = userDao.getUser(username.trim().lowercase()) ?: return null
        val now = System.currentTimeMillis()
        val twentyFourHours = 24 * 60 * 60 * 1000L
        val shouldReset = (now - user.lastResetTimestamp) > twentyFourHours

        val baseLives = if (shouldReset) 10 else user.livesCount
        val newLives = (baseLives - 1).coerceAtLeast(0)

        val updated = user.copy(
            totalXp = if (shouldReset) xpDelta else user.totalXp + xpDelta,
            gemsCount = if (shouldReset) gemsDelta else user.gemsCount + gemsDelta,
            lessonsCompleted = if (shouldReset) lessonDelta else user.lessonsCompleted + lessonDelta,
            gamesPlayed = if (shouldReset) gameDelta else user.gamesPlayed + gameDelta,
            livesCount = newLives,
            lastResetTimestamp = if (shouldReset) now else user.lastResetTimestamp
        )
        userDao.updateUser(updated)

        return UserResponse(
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
    }

    fun close() {
        // No-op
    }
}
