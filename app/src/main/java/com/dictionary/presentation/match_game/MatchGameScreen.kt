package com.dictionary.presentation.match_game

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.match_game.components.DropDownMenu
import com.dictionary.presentation.components.MatchGame
import com.dictionary.presentation.learn_words.LearnWordsEvent
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MatchGameScreen(
    onPopBackStack: () -> Unit = {},
    viewModel: MatchGameViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack()
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        topBar = {
            DropDownMenu(onPopBackStack)
        }
    ) { _ ->
        Column(modifier = Modifier.fillMaxSize()) {
            if (viewModel.matchState.currentWordsGroup.size == 0) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No words to repeat")
                    Button(onClick = { onPopBackStack() }) {
                        Text(text = "Back")
                    }
                }
            } else {
                MatchGame(
                    words = viewModel.matchState.currentWordsGroup,
                    onItemClick = { viewModel.onEvent(MatchGameEvent.OnMatchSelect(it)) },
                    stateMap = viewModel.matchState.wordsState,
                    totalCount = viewModel.matchState.totalCountWordsCount
                )
            }
        }
    }
}