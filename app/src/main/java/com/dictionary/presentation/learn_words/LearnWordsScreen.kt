package com.dictionary.presentation.learn_words

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.learn_words.components.DropDownMenu
import com.dictionary.presentation.learn_words.components.*
import com.dictionary.utils.UiEvent

@Composable
fun LearnWordsScreen(
    onPopBackStack: () -> Unit,
    viewModel: LearnWordsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val progress = remember { Animatable(0f) }
    LaunchedEffect(key1 = viewModel.learnProgress.value) {
        progress.animateTo(
            targetValue = viewModel.learnProgress.value,
            animationSpec = tween(
                durationMillis = 150,
                delayMillis = 0
            )
        )
    }

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
                Column{
                    if (viewModel.currentWords.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .height(15.dp)
                                    .clip(RoundedCornerShape(50f)),
                                progress = progress.value
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
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
}