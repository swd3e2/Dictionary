package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Write(viewModel: LearnWordsViewModel) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.writeState.currentWord.value?.let { word ->
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 20.dp),
                ) {
                    Text(text = word.term, fontSize = 32.sp)
                    if (viewModel.writeState.hasError.value) {
                        Column(
                            modifier = Modifier.padding(5.dp, 15.dp)
                        ) {
                            Text(text = "Ваш ответ", color = Color(0xFFC50A0A), fontSize = 12.sp)
                            Text(text = viewModel.writeState.hasDefinition.value)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Правильный ответ", color = Color(0xFF10AC2A), fontSize = 12.sp)
                            Text(text = viewModel.writeState.wantDefinition.value)
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            TextField(
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp),
                label = { Text(text = "Definition")},
                value = viewModel.writeState.definition.value,
                onValueChange = { viewModel.writeState.definition.value = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.onEvent(LearnWordsEvent.OnWriteTryDefinition(viewModel.writeState.definition.value))
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
        }
    }
}