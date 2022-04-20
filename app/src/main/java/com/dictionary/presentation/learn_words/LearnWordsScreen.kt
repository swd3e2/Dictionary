package com.dictionary.presentation.learn_words

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.learn_words.components.DropDownMenu
import com.dictionary.presentation.learn_words.components.*

@Composable
fun LearnWordsScreen(
    onPopBackStack: () -> Unit,
    viewModel: LearnWordsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding(),
        topBar = { DropDownMenu(onPopBackStack) }
    ) { padding ->
        when (viewModel.isLoading.value) {
            true -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            false -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedPage(viewModel.currentStep, 1) { Preview(onPopBackStack = onPopBackStack, viewModel = viewModel) }
                    AnimatedPage(viewModel.currentStep, 2) { Match(viewModel = viewModel) }
                    AnimatedPage(viewModel.currentStep, 3) { Test(viewModel = viewModel) }
                    AnimatedPage(viewModel.currentStep, 4) { Cards(viewModel = viewModel) }
                    AnimatedPage(viewModel.currentStep, 5) { Write(viewModel = viewModel) }
                    AnimatedPage(viewModel.currentStep, 6) { Done(onPopBackStack = onPopBackStack, viewModel = viewModel) }
                }
            }
        }
    }
}