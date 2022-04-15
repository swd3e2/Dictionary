package com.dictionary.presentation.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.dictionary.presentation.models.WordWithIndex
import com.dictionary.ui.theme.PrimaryTextColor
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun MatchGame(
    words: MutableList<WordWithIndex>,
    onItemClick: (WordWithIndex) -> Unit,
    stateMap: MutableMap<Int, String>,
    totalCount: Int
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val padding = PaddingValues(5.dp)

    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        for (word in words) {
            Box(
                modifier = Modifier
                    .width(screenWidth / 3 - padding.calculateStartPadding(LayoutDirection.Ltr))
                    .padding(5.dp)
                    .height(screenHeight / 4 - padding.calculateTopPadding() - 20.dp)
            ) {
                AnimatedVisibility(
                    visible = stateMap[word.index] != "success",
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier.clickable{ onItemClick(word) }.fillMaxSize(),
                        elevation = 0.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = when (stateMap[word.index]) {
                                        "selected" -> MaterialTheme.colors.primary
                                        "error" -> Color(0xFFD53F3F)
                                        "success" -> Color(0xFF61CF54)
                                        else -> MaterialTheme.colors.surface
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (totalCount > word.index) word.word.term else word.word.definition,
                                textAlign = TextAlign.Center,
                                color = when (stateMap[word.index]) {
                                    "selected" -> Color.White
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
}