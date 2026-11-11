package com.example.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.QuizTopBar
import com.example.ui.theme.PastelGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.util.SoundManager
import com.example.viewmodel.EduPlayViewModel

@Composable
fun AnimalQuizScreen(
    viewModel: EduPlayViewModel,
    soundManager: SoundManager?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.animalQuizState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F4))
            .padding(bottom = 16.dp)
    ) {
        QuizTopBar(
            progress = 0.65f,
            onCloseClick = onClose
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Animals Quiz",
                style = MaterialTheme.typography.labelLarge,
                color = TextMuted,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Which animal is this?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Center Giraffe Illustration
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
                    .size(240.dp)
                    .clickable {
                        soundManager?.speak("Giraffe!")
                    }
                    .testTag("quiz_animal_image")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_animal_giraffe),
                    contentDescription = "Giraffe Illustration",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2x2 Answer Grid
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
                QuizChoiceButton(
                    text = state.choices[0], // Elephant
                    isSelected = state.selectedChoice == state.choices[0],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak(state.choices[0])
                        viewModel.selectAnimalChoice(state.choices[0])
                    }
                )
                QuizChoiceButton(
                    text = state.choices[1], // Giraffe
                    isSelected = state.selectedChoice == state.choices[1],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak(state.choices[1])
                        viewModel.selectAnimalChoice(state.choices[1])
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuizChoiceButton(
                    text = state.choices[2], // Zebra
                    isSelected = state.selectedChoice == state.choices[2],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak(state.choices[2])
                        viewModel.selectAnimalChoice(state.choices[2])
                    }
                )
                QuizChoiceButton(
                    text = state.choices[3], // Lion
                    isSelected = state.selectedChoice == state.choices[3],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        soundManager?.speak(state.choices[3])
                        viewModel.selectAnimalChoice(state.choices[3])
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Continue Blue Pill Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Button(
                onClick = {
                    soundManager?.speak("Great job! It is a Giraffe!")
                    viewModel.continueAnimalQuiz()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("quiz_continue_button"),
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
fun QuizChoiceButton(
    text: String,
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
            .testTag("quiz_choice_${text.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) PrimaryGreen else TextDark
        )
    }
}
