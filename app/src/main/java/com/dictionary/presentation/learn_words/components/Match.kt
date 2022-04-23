package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dictionary.presentation.components.MatchGame
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel

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