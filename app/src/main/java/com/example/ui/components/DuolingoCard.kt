package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DuolingoCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    shadowColor: Color = Color(0xFFE2E8F0), // 3D bottom shadow color
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val boxModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Box(modifier = boxModifier) {
        // Bottom 3D shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(shadowColor)
        )
        // Top content layer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(containerColor)
                .padding(16.dp)
        ) {
            content()
        }
    }
}
