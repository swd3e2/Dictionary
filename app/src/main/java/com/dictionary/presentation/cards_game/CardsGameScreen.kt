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
import com.dictionary.presentation.match_game.components.DropDownMenu
import com.dictionary.presentation.components.WordCard
import com.dictionary.ui.theme.PrimaryTextColor

@Composable
fun CardsGameScreen(
    onPopBackStack: () -> Unit = {},
    viewModel: CardsGameViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        topBar = {
            DropDownMenu(onPopBackStack)
        }
    ) { _ ->
        when (viewModel.isLoading.value) {
            true -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            false -> {
                if (viewModel.currentWordIndex.value < viewModel.countOfWords.value) {
                    Progress(viewModel)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.currentWord.value != null) {
                        WordCard(
                            onLeftSwipe = { viewModel.onEvent(CardsGameEvent.WordNotLearned(viewModel.currentWord.value!!)) },
                            onRightSwipe = { viewModel.onEvent(CardsGameEvent.WordLearned(viewModel.currentWord.value!!)) },
                            word = viewModel.currentWord.value!!
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No words to repeat")
                            Button(onClick = { onPopBackStack() }) {
                                Text(text = "Back")
                            }
                        }
                    }
                    if (viewModel.currentWordIndex.value < viewModel.countOfWords.value) {
                        Counter(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun Counter(viewModel: CardsGameViewModel) {
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

@Composable
private fun Progress(viewModel: CardsGameViewModel) {
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
}