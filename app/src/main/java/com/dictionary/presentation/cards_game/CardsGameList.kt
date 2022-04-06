package com.dictionary.presentation.cards_game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.cards_game.components.DropDownMenu
import com.dictionary.presentation.cards_game.components.WordCard
import com.dictionary.ui.theme.PrimaryTextColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardsGameList(
    onPopBackStack: () -> Unit = {},
    viewModel: CardsGameViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            DropDownMenu(onPopBackStack)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${viewModel.currentWordIndex.value} / ${viewModel.countOfWords.value}",
                    letterSpacing = 3.sp,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryTextColor
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator(progress = viewModel.learnProgress.value)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.currentWord.value != null) {
                WordCard(onEvent = viewModel::onEvent, word = viewModel.currentWord.value!!)
            } else {
                Text(text = "No words to repeat")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(0.dp, 250.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .offset((-5).dp, 0.dp)
                        .background(
                            color = Color(0xFFFFDC28),
                            shape = RoundedCornerShape(10f)
                        )
                        .width(26.dp)
                        .height(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.offset(2.dp),
                        text = viewModel.notLearnedWordsCount.value.toString(),
                        color = Color(0xffffffff),
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(5.dp, 0.dp)
                        .background(
                            color = Color(0xFF16C054),
                            shape = RoundedCornerShape(10f)
                        )
                        .width(26.dp)
                        .height(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.offset((-2).dp),
                        text = viewModel.learnedWordsCount.value.toString(),
                        color = Color(0xffffffff)
                    )
                }
            }
        }
    }
}