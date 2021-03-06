package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel

@Composable
fun Done(viewModel: LearnWordsViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Congratulations",
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { viewModel.onEvent(LearnWordsEvent.OnBack(true)) }) {
                Text(text = "Back")
            }
            Button(onClick = { viewModel.onEvent(LearnWordsEvent.OnStartNew) }) {
                Text(text = "Next")
            }
        }
    }
}