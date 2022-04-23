package com.dictionary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerticalProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color
) {
    Column(
        modifier = modifier
            .width(16.dp)
    ) {
        if (progress < 1f) {
            Box(
                modifier = Modifier
                    .weight((if ((1 - progress) == 0f) 0.0001 else 1 - progress) as Float)
                    .fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .weight(progress)
                .fillMaxWidth()
                .background(
                    color = color
                )
        )
    }
}