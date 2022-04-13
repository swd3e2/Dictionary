package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dictionary.presentation.cards_game.components.WordCard
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel

@Composable
fun Done(viewModel: LearnWordsViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Congratulations")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {  }) {
                Text(text = "Back")
            }
            if (viewModel.wordsToLearn.size > 0) {
                Button(onClick = { viewModel.onEvent(LearnWordsEvent.OnStartNew) }) {
                    Text(text = "Next")
                }
            }
        }
    }
}