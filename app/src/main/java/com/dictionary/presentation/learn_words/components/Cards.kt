package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dictionary.presentation.components.WordCard
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel

@Composable
fun Cards(viewModel: LearnWordsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        viewModel.cardsState.currentWord.value?.let { word ->
            WordCard(
                onLeftSwipe = {
                    viewModel.onEvent(LearnWordsEvent.OnCardLeftSwipe(word))
                }, onRightSwipe = {
                    viewModel.onEvent(LearnWordsEvent.OnCardRightSwipe(word))
                }, word = word
            )
        }
    }
}