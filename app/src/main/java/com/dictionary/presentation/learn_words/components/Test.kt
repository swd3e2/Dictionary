package com.dictionary.presentation.learn_words.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
                modifier = Modifier.fillMaxSize().padding(0.dp, 0.dp, 0.dp, 25.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                for (suggestedWord in wordWithSuggest.suggested) {
                    Box(
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
                            }
                            .background(
                                color = when (viewModel.testState.wordsState[suggestedWord.index]) {
                                    "selected" -> MaterialTheme.colors.primary
                                    "error" -> Color(0xFFD53F3F)
                                    "success" -> Color(0xFF61CF54)
                                    else -> MaterialTheme.colors.surface
                                }
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(15.dp),
                            text = suggestedWord.word.definition,
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis,
                            color = when (viewModel.testState.wordsState[suggestedWord.index]) {
                                "error" -> Color.White
                                "success" -> Color.White
                                else -> PrimaryTextColor
                            }
                        )
                    }
                }
            }
        }
    }
}