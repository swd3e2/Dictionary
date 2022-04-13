package com.dictionary.presentation.learn_words.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedPage(
    currentStep: MutableState<Int>,
    page: Int,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

        AnimatedVisibility(
            visible = currentStep.value == page,
            exit = slideOutHorizontally { with(density) { -400.dp.roundToPx() } } + fadeOut(),
            enter = slideInHorizontally { with(density) { 400.dp.roundToPx() } } + fadeIn()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
    }
}