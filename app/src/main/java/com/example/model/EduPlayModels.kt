package com.example.model

import com.example.R

data class UserStats(
    val dayStreak: Int = 18,
    val totalXp: Int = 4860,
    val badgesCount: Int = 18,
    val gemsCount: Int = 25,
    val lessonsCompleted: Int = 32,
    val gamesPlayed: Int = 24,
    val longestStreak: Int = 32,
    val dailyGoalCompleted: Int = 2,
    val dailyGoalTotal: Int = 3,
    val livesCount: Int = 10
)

data class UserProfile(
    val name: String = "Emma",
    val fullName: String = "Emma Johnson",
    val handle: String = "@emma.j",
    val joinedYear: String = "2024",
    val avatarRes: Int = R.drawable.img_avatar_emma
)

enum class AppTab {
    HOME, GAMES, REWARD, PROFILE
}

sealed class AppDestination {
    data object MainTabs : AppDestination()
    data object AlphabetCourse : AppDestination()
    data class TraceLetter(val letter: String = "B") : AppDestination()
    data class WordBuilder(val wordIndex: Int = 0) : AppDestination()
    data class AnimalQuiz(val questionIndex: Int = 0) : AppDestination()
    data object NumberQuiz : AppDestination()
    data object MatchIt : AppDestination()
    data object StreakDetail : AppDestination()
    data object BadgesList : AppDestination()
}

data class CategoryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val iconBadgeText: String? = null,
    val bgHex: Long,
    val borderHex: Long,
    val iconBgHex: Long,
    val progress: Int = 12,
    val total: Int = 20
)

data class BadgeData(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val badgeColorHex: Long,
    val description: String,
    val earnedAgo: String? = null,
    val isEarned: Boolean = true
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val role: String,
    val xp: Int,
    val isCurrentUser: Boolean = false,
    val avatarEmoji: String
)

data class AnimalItem(
    val id: String,
    val name: String,
    val emoji: String,
    val soundText: String
)
