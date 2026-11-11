package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.QuizTopBar
import com.example.ui.theme.PastelPurple
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.util.SoundManager
import com.example.viewmodel.EduPlayViewModel

@Composable
fun TraceLetterScreen(
    letter: String = "B",
    viewModel: EduPlayViewModel,
    soundManager: SoundManager?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tracedPoints by viewModel.tracedPoints.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F4))
            .padding(bottom = 16.dp)
    ) {
        QuizTopBar(
            progress = 0.35f,
            onCloseClick = onClose
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Trace the Letter",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Follow the dots to write",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Large Tracing Canvas Area inside Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Interactive Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    viewModel.addTracedPoint(offset)
                                },
                                onDrag = { change, _ ->
                                    viewModel.addTracedPoint(change.position)
                                }
                            )
                        }
                        .testTag("tracing_canvas")
                ) {
                    val w = size.width
                    val h = size.height
                    val left = w * 0.22f
                    val top = h * 0.12f
                    val bottom = h * 0.88f
                    val midY = (top + bottom) / 2f
                    val rightUpper = w * 0.72f
                    val rightLower = w * 0.78f

                    // 1. Draw Giant Outlined Background Guide for Letter B
                    val guidePath = Path().apply {
                        moveTo(left, top)
                        lineTo(left, bottom)
                        // Bottom lobe
                        cubicTo(rightLower, bottom, rightLower, midY, left, midY)
                        // Top lobe
                        cubicTo(rightUpper, midY, rightUpper, top, left, top)
                    }

                    // Soft purple filled silhouette
                    drawPath(
                        path = guidePath,
                        color = Color(0xFFF3EAFD)
                    )

                    // Dotted guide stroke
                    drawPath(
                        path = guidePath,
                        color = Color(0xFFC4B5FD),
                        style = Stroke(
                            width = 6.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 18f), 0f)
                        )
                    )

                    // 2. Draw Start Dot ① at (left, top)
                    drawCircle(
                        color = PrimaryPurple,
                        radius = 16.dp.toPx(),
                        center = Offset(left, top)
                    )

                    // 3. Draw User's Drawn Trace in Vivid Purple/Violet
                    if (tracedPoints.size > 1) {
                        val userPath = Path()
                        userPath.moveTo(tracedPoints.first().x, tracedPoints.first().y)
                        for (i in 1 until tracedPoints.size) {
                            userPath.lineTo(tracedPoints[i].x, tracedPoints[i].y)
                        }
                        drawPath(
                            path = userPath,
                            color = PrimaryPurple,
                            style = Stroke(
                                width = 16.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                // Number 1 badge overlay on start point
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 24.dp, top = 20.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Helper Tip Banner with Pencil Character
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PastelPurple)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "⭐", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start at the top and drag your finger.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryPurple
                    )
                }
                Text(text = "✏️", fontSize = 26.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Controls: Reset & Done Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Reset Button
            IconButton(
                onClick = { viewModel.clearTracing() },
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .testTag("reset_tracing_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Tracing",
                    tint = TextDark,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Done Button
            Button(
                onClick = {
                    soundManager?.speak("Great job writing letter B!")
                    viewModel.completeTracing()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("done_tracing_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {
                Text(
                    text = "Done",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
