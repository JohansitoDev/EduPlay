package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
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
import com.example.ui.components.SubScreenTopBar
import com.example.ui.theme.PastelGreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.util.SoundManager

@Composable
fun AlphabetCourseScreen(
    soundManager: SoundManager?,
    onNavigate: (AppDestination) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F4))
    ) {
        SubScreenTopBar(
            title = null,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: ABC blocks + Title
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PastelGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A B C",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryGreen,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Alphabet",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Learn letters, sounds and build strong basics.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress bar: 8 / 26
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { 8f / 26f },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PrimaryGreen,
                            trackColor = Color(0xFFE2F0E8)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "8 / 26",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Featured Card: Letter of the Day
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("letter_of_the_day_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Letter of the Day",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            // Big stylized green letter B
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(PastelGreen)
                                    .border(2.dp, Color(0xFF86EFAC), RoundedCornerShape(20.dp))
                                    .clickable {
                                        soundManager?.speak("B is for Ball!")
                                        onNavigate(AppDestination.TraceLetter("B"))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "B",
                                    fontSize = 54.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PrimaryGreen
                                )
                            }

                            // Beach ball emoji/icon
                            Text(
                                text = "🏐",
                                fontSize = 56.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "B is for Ball",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }

                            // Play Sound button
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryGreen)
                                    .clickable {
                                        soundManager?.speak("B is for Ball!")
                                    }
                                    .testTag("play_sound_b"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Listen",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Lessons & Activities
            item {
                Text(
                    text = "Interactive Activities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Lesson 1: A is for Apple (Completed)
            item {
                LessonRowCard(
                    icon = "🍎",
                    title = "A is for Apple",
                    isCompleted = true,
                    isLocked = false,
                    onClick = { soundManager?.speak("A is for Apple!") }
                )
            }

            // Activity 2: Trace the Letter B
            item {
                LessonRowCard(
                    icon = "✏️",
                    title = "Trace the Letter",
                    subtitle = "Practice writing letter B with guide dots",
                    isCompleted = false,
                    isLocked = false,
                    onClick = { onNavigate(AppDestination.TraceLetter("B")) }
                )
            }

            // Activity 3: What Letter Is This? (Match It)
            item {
                LessonRowCard(
                    icon = "🐱",
                    title = "What Letter Is This?",
                    subtitle = "Match letters to animals and pictures",
                    isCompleted = false,
                    isLocked = false,
                    onClick = { onNavigate(AppDestination.MatchIt) }
                )
            }

            // Activity 4: Word Builder (Spelling)
            item {
                LessonRowCard(
                    icon = "🧩",
                    title = "Word Builder",
                    subtitle = "Build words with colorful tiles",
                    isCompleted = false,
                    isLocked = false,
                    onClick = { onNavigate(AppDestination.WordBuilder(0)) }
                )
            }

            // Lesson 5: C is for Cat (Locked)
            item {
                LessonRowCard(
                    icon = "🐱",
                    title = "C is for Cat",
                    isCompleted = false,
                    isLocked = true,
                    onClick = {}
                )
            }

            // Lesson 6: D is for Dog (Locked)
            item {
                LessonRowCard(
                    icon = "🐶",
                    title = "D is for Dog",
                    isCompleted = false,
                    isLocked = true,
                    onClick = {}
                )
            }

            // Lesson 7: E is for Elephant (Locked)
            item {
                LessonRowCard(
                    icon = "🐘",
                    title = "E is for Elephant",
                    isCompleted = false,
                    isLocked = true,
                    onClick = {},
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
fun LessonRowCard(
    icon: String,
    title: String,
    subtitle: String? = null,
    isCompleted: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isLocked) { onClick() }
            .testTag("lesson_row_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLocked) Color(0xFFF8FAFC) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLocked) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isLocked) TextMuted else TextDark
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Status Icon
            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEBF3FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
