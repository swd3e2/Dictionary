package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Write(viewModel: LearnWordsViewModel) {
    val word = viewModel.writeState.currentWord.value!!
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = word.term)
                Text(text = viewModel.writeState.wantDefinition.value)
            }
        }
        Spacer(Modifier.weight(1f))
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = viewModel.writeState.definition.value,
            onValueChange = { viewModel.writeState.definition.value = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onEvent(LearnWordsEvent.OnWriteTryDefinition(viewModel.writeState.definition.value)) }
            ),
        )
    }
}