package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val username: String,
    val password: String,
    val name: String,
    val fullName: String,
    val handle: String,
    val dayStreak: Int = 18,
    val totalXp: Int = 4860,
    val badgesCount: Int = 18,
    val gemsCount: Int = 25,
    val lessonsCompleted: Int = 32,
    val gamesPlayed: Int = 24,
    val livesCount: Int = 10,
    val lastResetTimestamp: Long = System.currentTimeMillis(),
    val isCurrent: Boolean = false
)
