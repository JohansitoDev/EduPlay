package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppDestination
import com.example.model.AppTab
import com.example.model.UserProfile
import com.example.model.UserStats
import com.example.ui.components.DuolingoCard
import com.example.ui.components.MainTopBar
import com.example.ui.theme.PastelBlue
import com.example.ui.theme.PastelGreen
import com.example.ui.theme.PastelOrange
import com.example.ui.theme.PastelPurple
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun HomeScreen(
    stats: UserStats,
    profile: UserProfile,
    onNavigate: (AppDestination) -> Unit,
    onSelectTab: (AppTab) -> Unit,
    onToggleProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showQuickMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        MainTopBar(
            streakCount = stats.dayStreak,
            gemsCount = stats.gemsCount,
            onMenuClick = { showQuickMenu = !showQuickMenu },
            onStreakClick = { onNavigate(AppDestination.StreakDetail) }
        )

        // Dropdown menu drawer when hamburger is tapped
        AnimatedVisibility(visible = showQuickMenu) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Acciones Rápidas 🚀",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onToggleProfile()
                                showQuickMenu = false
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔄 Cambiar Usuario: ${profile.name}", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showQuickMenu = false
                                onNavigate(AppDestination.StreakDetail)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔥 Ver Calendario de Racha", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting & Avatar Header with hand wave
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "¡Hola, ${profile.name}! 👋",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "¡Aprendamos algo nuevo hoy!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFFFD166), CircleShape)
                            .clickable { onSelectTab(AppTab.PROFILE) }
                            .testTag("home_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_avatar_emma),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Streak 3D Card
            item {
                DuolingoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_streak_card"),
                    containerColor = Color.White,
                    shadowColor = Color(0xFFE2E8F0),
                    onClick = { onNavigate(AppDestination.StreakDetail) }
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🔥", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${stats.dayStreak}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = PrimaryOrange,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "días de racha",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Days of week row
                        val days = listOf("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            days.forEachIndexed { index, day ->
                                val isPastDay = index <= 4
                                val isToday = index == 4
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isToday) PrimaryOrange else TextMuted,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isToday) Color(0xFFFF7A00)
                                                else if (isPastDay) Color(0xFFFF9E40)
                                                else Color(0xFFF1F5F9)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isToday) {
                                            Text(
                                                text = "19",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        } else if (isPastDay) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Listo",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "¡Vas por buen camino! 🔥 ¡Sigue así!",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFFD97706),
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Detalles",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Learn Categories Section
            item {
                Text(
                    text = "Categorías de Aprendizaje",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            // 2x2 Grid of 3D Duolingo Category Cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Row 1: Alphabet & Numbers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CategoryGridCard(
                            title = "Alfabeto",
                            iconEmoji = "🔤",
                            badgeText = "A B C",
                            bgColor = PastelGreen,
                            accentColor = Color(0xFF16A34A),
                            modifier = Modifier.weight(1f),
                            testTag = "category_alphabet",
                            onClick = { onNavigate(AppDestination.AlphabetCourse) }
                        )

                        CategoryGridCard(
                            title = "Números",
                            iconEmoji = "🔢",
                            badgeText = "1 2 3",
                            bgColor = PastelBlue,
                            accentColor = PrimaryBlue,
                            modifier = Modifier.weight(1f),
                            testTag = "category_numbers",
                            onClick = { onNavigate(AppDestination.NumberQuiz) }
                        )
                    }

                    // Row 2: Animals & Colors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CategoryGridCard(
                            title = "Animales",
                            iconEmoji = "🦁",
                            badgeText = null,
                            bgColor = PastelOrange,
                            accentColor = Color(0xFFEA580C),
                            modifier = Modifier.weight(1f),
                            testTag = "category_animals",
                            onClick = { onNavigate(AppDestination.AnimalQuiz(0)) }
                        )

                        CategoryGridCard(
                            title = "Colores",
                            iconEmoji = "🎨",
                            badgeText = null,
                            bgColor = PastelPurple,
                            accentColor = Color(0xFF9333EA),
                            modifier = Modifier.weight(1f),
                            testTag = "category_colors",
                            onClick = { onNavigate(AppDestination.WordBuilder(0)) }
                        )
                    }
                }
            }

            // Today's Goal 3D Card
            item {
                DuolingoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    containerColor = Color.White,
                    shadowColor = Color(0xFFE2E8F0)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Meta de Hoy",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextDark,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Completa ${stats.dailyGoalTotal} actividades",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { stats.dailyGoalCompleted.toFloat() / stats.dailyGoalTotal.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFFEC4899),
                                trackColor = Color(0xFFFCE7F3)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "⭐", fontSize = 26.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGridCard(
    title: String,
    iconEmoji: String,
    badgeText: String?,
    bgColor: Color,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    DuolingoCard(
        modifier = modifier
            .height(130.dp)
            .testTag(testTag),
        containerColor = bgColor,
        shadowColor = accentColor.copy(alpha = 0.35f),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (badgeText != null) {
                        Text(
                            text = badgeText,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor,
                            fontSize = 12.sp
                        )
                    } else {
                        Text(text = iconEmoji, fontSize = 22.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ir",
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }
}
