package com.example.data.local

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val currentUser: Flow<UserEntity?> = userDao.getCurrentUser()

    suspend fun loginOrRegister(username: String, password: String): UserEntity {
        val trimmed = username.trim().lowercase()
        val existing = userDao.getUser(trimmed)
        userDao.clearCurrentUsers()
        val now = System.currentTimeMillis()
        val twentyFourHours = 24 * 60 * 60 * 1000L

        if (existing != null) {
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
            return updated
        } else {
            val newUser = UserEntity(
                username = trimmed,
                password = password,
                name = username.replaceFirstChar { it.uppercase() },
                fullName = username.replaceFirstChar { it.uppercase() + " Estudiante" },
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
            return newUser
        }
    }

    suspend fun updateStats(username: String, xpDelta: Int, gemsDelta: Int, streakDelta: Int, lessonDelta: Int, gameDelta: Int) {
        val user = userDao.getUser(username) ?: return
        val now = System.currentTimeMillis()
        val twentyFourHours = 24 * 60 * 60 * 1000L
        val shouldReset = (now - user.lastResetTimestamp) > twentyFourHours

        val baseLives = if (shouldReset) 10 else user.livesCount
        val newLives = (baseLives - 1).coerceAtLeast(0)

        val updated = user.copy(
            totalXp = if (shouldReset) xpDelta else user.totalXp + xpDelta,
            gemsCount = if (shouldReset) gemsDelta else user.gemsCount + gemsDelta,
            dayStreak = if (shouldReset) 1 else user.dayStreak + streakDelta,
            lessonsCompleted = if (shouldReset) lessonDelta else user.lessonsCompleted + lessonDelta,
            gamesPlayed = if (shouldReset) gameDelta else user.gamesPlayed + gameDelta,
            livesCount = newLives,
            lastResetTimestamp = if (shouldReset) now else user.lastResetTimestamp
        )
        userDao.updateUser(updated)
    }

    suspend fun logout(username: String) {
        val user = userDao.getUser(username)
        if (user != null) {
            userDao.updateUser(user.copy(isCurrent = false))
        }
        userDao.clearCurrentUsers()
    }
}
