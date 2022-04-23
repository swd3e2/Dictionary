package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.presentation.learn_words.LearnWordsViewModel
import com.dictionary.ui.theme.PrimaryTextColor

@Composable
fun Test(viewModel: LearnWordsViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        viewModel.testState.currentWord.value?.let { wordWithSuggest ->
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = wordWithSuggest.word.term,
                    fontSize = 32.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 25.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                for (suggestedWord in wordWithSuggest.suggested) {
                    Card(
                        modifier = Modifier
                            .padding(15.dp, 5.dp)
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(
                                    LearnWordsEvent.OnTestSelect(
                                        wordWithSuggest.word,
                                        suggestedWord
                                    )
                                )
                            },
                        backgroundColor = when (viewModel.testState.wordsState[suggestedWord.index]) {
                            "selected" -> MaterialTheme.colors.primary
                            "error" -> MaterialTheme.colors.error
                            "success" -> if (MaterialTheme.colors.isLight) Color(0xFF61CF54) else Color(
                                0xFF81B977
                            )
                            else -> MaterialTheme.colors.surface
                        },
                        elevation = 2.dp,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                modifier = Modifier.padding(15.dp),
                                text = suggestedWord.word.definition,
                                style = MaterialTheme.typography.body1,
                                overflow = TextOverflow.Ellipsis,
                                color = when (viewModel.testState.wordsState[suggestedWord.index]) {
                                    "error" -> MaterialTheme.colors.onError
                                    "success" -> MaterialTheme.colors.onError
                                    else -> MaterialTheme.colors.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}