package com.dictionary.presentation.learn_words.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.dictionary.presentation.components.MatchGame
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel
import com.dictionary.ui.theme.PrimaryTextColor
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun Match(
    viewModel: LearnWordsViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        MatchGame(
            words = viewModel.matchState.currentWordsGroup,
            onItemClick = { viewModel.onEvent(LearnWordsEvent.OnMatchSelect(it)) },
            stateMap = viewModel.matchState.wordsState
        )
    }
}