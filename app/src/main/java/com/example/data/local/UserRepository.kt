package com.example.data.local

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val currentUser: Flow<UserEntity?> = userDao.getCurrentUser()

    suspend fun loginOrRegister(username: String, password: String): UserEntity {
        val trimmed = username.trim().lowercase()
        val existing = userDao.getUser(trimmed)
        userDao.clearCurrentUsers()
        if (existing != null) {
            val updated = existing.copy(isCurrent = true)
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
                lessonsCompleted = 1,
                gamesPlayed = 0,
                isCurrent = true
            )
            userDao.insertUser(newUser)
            return newUser
        }
    }

    suspend fun updateStats(username: String, xpDelta: Int, gemsDelta: Int, streakDelta: Int, lessonDelta: Int, gameDelta: Int) {
        val user = userDao.getUser(username) ?: return
        val updated = user.copy(
            totalXp = user.totalXp + xpDelta,
            gemsCount = user.gemsCount + gemsDelta,
            dayStreak = user.dayStreak + streakDelta,
            lessonsCompleted = user.lessonsCompleted + lessonDelta,
            gamesPlayed = user.gamesPlayed + gameDelta
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
