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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.theme.PastelPurple
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.util.SoundManager
import com.example.viewmodel.EduPlayViewModel

@Composable
fun WordBuilderScreen(
    viewModel: EduPlayViewModel,
    soundManager: SoundManager?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.wordBuilderState.collectAsStateWithLifecycle()

    val imageRes = if (state.wordIndex == 1) {
        R.drawable.img_animal_zebra
    } else {
        R.drawable.img_animal_cow
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F4))
            .padding(bottom = 16.dp)
    ) {
        QuizTopBar(
            progress = 0.55f,
            onCloseClick = onClose
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Word Builder",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Build the word for this picture",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Center Animal Illustration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        soundManager?.speak(state.targetWord)
                    }
                    .testTag("word_builder_image_card")
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = state.targetWord,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Target Letter Slots (purple rounded boxes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.letterSlots.forEachIndexed { index, char ->
                val isFilled = char != null
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isFilled) Color(0xFFDDD6FE) else Color(0xFFF8FAFC))
                        .border(
                            width = 2.dp,
                            color = if (isFilled) Color(0xFFC4B5FD) else Color(0xFFCBD5E1),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(enabled = isFilled) {
                            viewModel.tapSlotLetter(index)
                        }
                        .testTag("letter_slot_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    if (char != null) {
                        Text(
                            text = char.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryPurple,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Letter Bank / Keyboard Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bank = state.letterBank
            val half = (bank.size + 1) / 2
            val row1 = bank.take(half)
            val row2 = bank.drop(half)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row1.forEach { letter ->
                    LetterTile(letter = letter, onClick = {
                        soundManager?.speak(letter.toString())
                        viewModel.tapBankLetter(letter)
                    })
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row2.forEach { letter ->
                    LetterTile(letter = letter, onClick = {
                        soundManager?.speak(letter.toString())
                        viewModel.tapBankLetter(letter)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom Bar: Hint button + Check Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Hint Button with badge count
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .clickable {
                        viewModel.useHint()
                        soundManager?.speak(state.hintText)
                    }
                    .testTag("hint_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Hint",
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(24.dp)
                )

                // Hint badge count
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEC4899)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${state.remainingHints}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Check Button
            Button(
                onClick = {
                    val correct = viewModel.checkWordBuilder()
                    if (correct) {
                        soundManager?.speak("Correct! ${state.targetWord}!")
                    } else {
                        soundManager?.speak("Try again! You can do it!")
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("check_word_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {
                Text(
                    text = "Check",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LetterTile(
    letter: Char,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("bank_letter_$letter"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            fontSize = 18.sp
        )
    }
}
