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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.model.UserStats
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun ProfileTabScreen(
    profile: UserProfile,
    stats: UserStats,
    soundEnabled: Boolean,
    notificationsEnabled: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAboutDialog by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }
    var selectedAvatarEmoji by remember { mutableStateOf("🦁") }

    val avatarList = listOf("🦁", "🐯", "🐼", "🦊", "🐰", "👧", "👦", "🐸", "🦄", "🦖")

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = {
                Text(
                    text = "Elige tu Avatar 🎨",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(avatarList) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(if (selectedAvatarEmoji == emoji) Color(0xFFE5FDCC) else Color(0xFFF0F0F0))
                                .clickable {
                                    selectedAvatarEmoji = emoji
                                    showAvatarDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 32.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text("Cerrar", color = PrimaryBlue)
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(
                    text = "Acerca de EduPlay",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "EduPlay está diseñado para despertar alegría, curiosidad y alfabetización en los primeros estudiantes a través de juegos interactivos, trazo de letras y fonética lúdica.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Cerrar", color = PrimaryBlue)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )

            IconButton(
                onClick = { showAboutDialog = true },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ajustes",
                    tint = TextDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card Header with Avatar & Edit
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar with edit pencil
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.clickable { showAvatarDialog = true }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE5FDCC)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = selectedAvatarEmoji, fontSize = 48.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Cambiar Avatar",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = profile.handle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            }

            // Graphical Statistics Section
            item {
                Text(
                    text = "Estadísticas Gráficas 📊",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Vidas restantes
                        StatProgressBarRow(
                            emoji = "❤️",
                            label = "Vidas Diarias",
                            value = "${stats.livesCount} / 10",
                            progress = stats.livesCount / 10f,
                            color = Color(0xFFFF4B4B)
                        )
                        // Racha
                        StatProgressBarRow(
                            emoji = "🔥",
                            label = "Racha de Días",
                            value = "${stats.dayStreak} días",
                            progress = (stats.dayStreak % 30) / 30f,
                            color = PrimaryOrange
                        )
                        // XP
                        StatProgressBarRow(
                            emoji = "⭐",
                            label = "Puntuación XP",
                            value = "${stats.totalXp} XP",
                            progress = (stats.totalXp % 5000) / 5000f,
                            color = Color(0xFFFFB703)
                        )
                        // Gemas
                        StatProgressBarRow(
                            emoji = "💎",
                            label = "Gemas Coleccionadas",
                            value = "${stats.gemsCount}",
                            progress = (stats.gemsCount % 100) / 100f,
                            color = PrimaryBlue
                        )
                        // Lecciones
                        StatProgressBarRow(
                            emoji = "📚",
                            label = "Lecciones Completadas",
                            value = "${stats.lessonsCompleted}",
                            progress = (stats.lessonsCompleted % 50) / 50f,
                            color = PrimaryGreen
                        )
                    }
                }
            }

            // Settings Section
            item {
                Text(
                    text = "Ajustes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Sonido y Voz", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Switch(
                                checked = soundEnabled,
                                onCheckedChange = onToggleSound,
                                colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Notificaciones", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = onToggleNotifications,
                                colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen)
                            )
                        }
                    }
                }
            }

            // Logout Button
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFEEEE))
                        .clickable { onToggleProfile() }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cerrar Sesión / Cambiar Usuario",
                        color = Color(0xFFFF4B4B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatProgressBarRow(
    emoji: String,
    label: String,
    value: String,
    progress: Float,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = emoji, fontSize = 20.sp)
                Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextDark)
            }
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextDark)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Color(0xFFF0F0F0)
        )
    }
}
