package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppDestination
import com.example.model.UserStats
import com.example.ui.components.MainTopBar
import com.example.ui.theme.PastelBlue
import com.example.ui.theme.PastelGreen
import com.example.ui.theme.PastelOrange
import com.example.ui.theme.PastelPurple
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun GamesTabScreen(
    stats: UserStats,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F4))
    ) {
        MainTopBar(
            streakCount = stats.dayStreak,
            gemsCount = stats.gemsCount,
            onMenuClick = {},
            onStreakClick = { onNavigate(AppDestination.StreakDetail) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Planet Icon
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Learn & Play",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Play games, learn new things and collect stars!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }

                    Text(text = "🪐", fontSize = 42.sp)
                }
            }

            // Daily Mission Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Daily Mission",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "⏱ 9h",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Complete 3 games today",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                progress = { 1f / 3f },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = PrimaryPurple,
                                trackColor = Color(0xFFF3EAFD)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(text = "🎁", fontSize = 24.sp)
                        }
                    }
                }
            }

            // Quiz Categories Header
            item {
                Text(
                    text = "Quiz Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Numbers Quiz Row
            item {
                GameCategoryRow(
                    badge = "1 2 3",
                    badgeBg = PastelBlue,
                    badgeColor = PrimaryBlue,
                    title = "Numbers",
                    subtitle = "Learn numbers and counting",
                    progressText = "12/20",
                    progressFloat = 12f / 20f,
                    accentColor = PrimaryBlue,
                    onClick = { onNavigate(AppDestination.NumberQuiz) }
                )
            }

            // Animals Quiz Row
            item {
                GameCategoryRow(
                    emoji = "🦁",
                    badgeBg = PastelOrange,
                    badgeColor = PrimaryOrange,
                    title = "Animals",
                    subtitle = "Discover amazing animals",
                    progressText = "16/20",
                    progressFloat = 16f / 20f,
                    accentColor = PrimaryOrange,
                    onClick = { onNavigate(AppDestination.AnimalQuiz(0)) }
                )
            }

            // Alphabet Quiz Row
            item {
                GameCategoryRow(
                    badge = "A B C",
                    badgeBg = PastelGreen,
                    badgeColor = PrimaryGreen,
                    title = "Alphabet",
                    subtitle = "Letters and phonics fun",
                    progressText = "14/20",
                    progressFloat = 14f / 20f,
                    accentColor = PrimaryGreen,
                    onClick = { onNavigate(AppDestination.AlphabetCourse) }
                )
            }

            // Shapes / Matching Row
            item {
                GameCategoryRow(
                    emoji = "🔷",
                    badgeBg = PastelPurple,
                    badgeColor = PrimaryPurple,
                    title = "Shapes & Matching",
                    subtitle = "Learn shapes and matching pairs",
                    progressText = "6/20",
                    progressFloat = 6f / 20f,
                    accentColor = PrimaryPurple,
                    onClick = { onNavigate(AppDestination.MatchIt) },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
fun GameCategoryRow(
    title: String,
    subtitle: String,
    progressText: String,
    progressFloat: Float,
    accentColor: Color,
    badgeBg: Color,
    badgeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null,
    emoji: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("game_row_${title.lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Badge / Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                if (badge != null) {
                    Text(
                        text = badge,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeColor,
                        fontSize = 12.sp
                    )
                } else if (emoji != null) {
                    Text(text = emoji, fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details & Progress
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progressFloat },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = accentColor,
                    trackColor = badgeBg
                )
            }

            // Play Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play $title",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
