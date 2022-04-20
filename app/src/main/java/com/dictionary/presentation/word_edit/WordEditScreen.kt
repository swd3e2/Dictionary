package com.dictionary.presentation.word_edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.presentation.word_edit.componetns.DropDownMenu
import com.dictionary.presentation.word_edit.componetns.TranslationDialog
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WordEditScreen(
    onPopBackStack: () -> Unit,
    viewModel: WordEditViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.PopBackStack -> {
                    focusManager.clearFocus()
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
            .navigationBarsPadding()
            .systemBarsPadding(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(WordEditEvent.OnSaveClick)
            }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Add")
            }
        },
        topBar = {
            DropDownMenu(viewModel.word?.term ?: "", onPopBackStack, viewModel.menuExpanded, viewModel::onEvent)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            if (viewModel.showTranslationDialog.value) {
                TranslationDialog(
                    translationState = viewModel.state,
                    selectedTranslations = viewModel.selectedTranslations,
                    onEvent = viewModel::onEvent
                )
            }

            TextField(
                value = viewModel.newTerm.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnTermChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                label = { Text(text = "Term")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newDefinition.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnDefinitionChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(WordEditEvent.OnShowTranslationDialog)
                            focusManager.clearFocus()
                        }) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                label = { Text(text = "Definition")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newAntonyms.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnAntonymsChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                label = { Text(text = "Antonyms")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newSynonyms.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnSynonymsChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                label = { Text(text = "Synonyms")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newSimilar.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnSimilarChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                label = { Text(text = "Similar")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newTranscription.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnTranscriptionChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                label = { Text(text = "Transcription")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
        }
    }
}