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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun NumberQuizScreen(
    viewModel: EduPlayViewModel,
    soundManager: SoundManager?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.numberQuizState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F4))
            .padding(bottom = 16.dp)
    ) {
        QuizTopBar(
            progress = 0.75f,
            onCloseClick = onClose
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Numbers",
                style = MaterialTheme.typography.labelLarge,
                color = TextMuted,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "How many apples?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Center Counting Area with 7 Red Apples
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .testTag("apples_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Row 1: 3 apples
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { idx ->
                            AppleItem(idx + 1, soundManager)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 2: 2 apples
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(2) { idx ->
                            AppleItem(idx + 4, soundManager)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 3: 2 apples
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(2) { idx ->
                            AppleItem(idx + 6, soundManager)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2x2 Answer Grid: 5, 6, 7, 8
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NumberChoiceButton(
                    number = state.choices[0],
                    isSelected = state.selectedChoice == state.choices[0],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak("${state.choices[0]}")
                        viewModel.selectNumberChoice(state.choices[0])
                    }
                )
                NumberChoiceButton(
                    number = state.choices[1],
                    isSelected = state.selectedChoice == state.choices[1],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak("${state.choices[1]}")
                        viewModel.selectNumberChoice(state.choices[1])
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NumberChoiceButton(
                    number = state.choices[2],
                    isSelected = state.selectedChoice == state.choices[2],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak("${state.choices[2]}")
                        viewModel.selectNumberChoice(state.choices[2])
                    }
                )
                NumberChoiceButton(
                    number = state.choices[3],
                    isSelected = state.selectedChoice == state.choices[3],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak("${state.choices[3]}")
                        viewModel.selectNumberChoice(state.choices[3])
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Continue Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Button(
                onClick = {
                    soundManager?.speak("Super! There are 7 apples!")
                    viewModel.continueNumberQuiz()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("number_quiz_continue_button"),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                )
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AppleItem(number: Int, soundManager: SoundManager?) {
    Text(
        text = "🍎",
        fontSize = 44.sp,
        modifier = Modifier
            .clickable {
                soundManager?.speak("$number")
            }
            .testTag("apple_$number")
    )
}

@Composable
fun NumberChoiceButton(
    number: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PastelGreen else Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PrimaryGreen else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .testTag("number_choice_$number"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$number",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) PrimaryGreen else TextDark,
            fontSize = 20.sp
        )
    }
}
