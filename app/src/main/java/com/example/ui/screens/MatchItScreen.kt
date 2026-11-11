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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.QuizTopBar
import com.example.ui.theme.PastelGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.util.SoundManager
import com.example.viewmodel.EduPlayViewModel

data class MatchAnimal(val id: String, val name: String, val emoji: String)

@Composable
fun MatchItScreen(
    viewModel: EduPlayViewModel,
    soundManager: SoundManager?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.matchItState.collectAsStateWithLifecycle()

    val animals = listOf(
        MatchAnimal("lion", "Lion", "🦁"),
        MatchAnimal("elephant", "Elephant", "🐘"),
        MatchAnimal("panda", "Panda", "🐼"),
        MatchAnimal("fox", "Fox", "🦊")
    )

    var selectedAnimal by remember { mutableStateOf<String?>("Lion") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F4))
            .padding(bottom = 16.dp)
    ) {
        QuizTopBar(
            progress = 0.85f,
            onCloseClick = onClose
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Match It",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Match the animals to their names",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Match Column Rows
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            animals.forEach { animal ->
                val isMatched = state.matches[animal.name] != null
                val isSelectedLeft = selectedAnimal == animal.name

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Animal Card
                    Card(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                selectedAnimal = animal.name
                                soundManager?.speak(animal.name)
                            }
                            .testTag("match_animal_${animal.name.lowercase()}"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelectedLeft) PastelGreen else Color.White
                        ),
                        border = if (isSelectedLeft) androidx.compose.foundation.BorderStroke(2.dp, PrimaryGreen) else null
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = animal.emoji, fontSize = 38.sp)
                        }
                    }

                    // Dot & Connecting Indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isMatched) PrimaryGreen else Color(0xFFCBD5E1))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(if (isMatched) 3.dp else 1.dp)
                                .background(if (isMatched) PrimaryGreen else Color(0xFFE2E8F0))
                        )
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isMatched) PrimaryGreen else Color(0xFFCBD5E1))
                        )
                    }

                    // Right Name Tag
                    val isConnectedToRight = state.matches[animal.name] == animal.name
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isConnectedToRight) PastelGreen else Color.White)
                            .border(
                                width = if (isConnectedToRight) 2.dp else 1.dp,
                                color = if (isConnectedToRight) PrimaryGreen else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                if (selectedAnimal != null) {
                                    viewModel.connectMatch(selectedAnimal!!, animal.name)
                                    soundManager?.speak("Matched ${animal.name}!")
                                }
                            }
                            .testTag("match_target_${animal.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = animal.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isConnectedToRight) PrimaryGreen else TextDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Check Answers Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Button(
                onClick = {
                    soundManager?.speak("Great job matching all the animals!")
                    viewModel.checkMatchAnswers()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("check_answers_button"),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                )
            ) {
                Text(
                    text = "Check Answers",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
