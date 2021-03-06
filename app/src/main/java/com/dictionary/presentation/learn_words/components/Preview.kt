package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor

@Composable
fun Preview(viewModel: LearnWordsViewModel) {
    when {
        viewModel.hasSavedState.value -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Have saved state, want to continue?")
                Button(onClick = { viewModel.onEvent(LearnWordsEvent.OnRestart) }) {
                    Text(text = "Restart")
                }
                Button(onClick = { viewModel.onEvent(LearnWordsEvent.OnContinue) }) {
                    Text(text = "Continue")
                }
            }
        }
        viewModel.currentWords.size == 0 -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No words to learn")
                Button(onClick = { viewModel.onEvent(LearnWordsEvent.OnBack(true)) }) {
                    Text(text = "Back")
                }
            }
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                for (word in viewModel.currentWords) {
                    Card(
                        modifier = Modifier.padding(15.dp, 5.dp),
                        elevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = word.term,
                                    style = MaterialTheme.typography.body1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.primary
                                )
                                Text(
                                    text = word.definition,
                                    style = MaterialTheme.typography.body1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.onSurface,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { viewModel.onEvent(LearnWordsEvent.OnGoToMatch) }) {
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}