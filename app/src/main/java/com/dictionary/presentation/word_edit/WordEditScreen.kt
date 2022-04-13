package com.dictionary.presentation.word_edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.word_edit.componetns.DropDownMenu
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WordEditScreen(
    onPopBackStack: () -> Unit,
    viewModel: WordEditViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
//    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is WordEditViewModel.UIEvent.ShowSnackbar -> {
//                    snackbarHostState.showSnackbar(
//                        message = event.message
//                    )
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
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(WordEditEvent.OnSaveClick)
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Add")
            }
        },
        topBar = {
            DropDownMenu(viewModel.word?.term ?: "", onPopBackStack, viewModel.menuExpanded, viewModel::onEvent)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = viewModel.newTerm.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnTermChange(it)) },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                label = { Text(text = "Term")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newDefinition.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnDefinitionChange(it)) },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                label = { Text(text = "Definition")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newAntonyms.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnAntonymsChange(it)) },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                label = { Text(text = "Antonyms")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newSynonyms.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnSynonymsChange(it)) },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                label = { Text(text = "Synonyms")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newSimilar.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnSimilarChange(it)) },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                label = { Text(text = "Similar")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            TextField(
                value = viewModel.newTranscription.value,
                onValueChange = { viewModel.onEvent(WordEditEvent.OnTranscriptionChange(it)) },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                label = { Text(text = "Transcription")},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
        }
//        SnackbarHost(
//            hostState = snackbarHostState,
//            snackbar = { snackbarData: SnackbarData ->
//                Card(
//                    shape = RoundedCornerShape(8.dp),
//                    border = BorderStroke(2.dp, Color.White),
//                    modifier = Modifier
//                        .padding(16.dp)
//
//                        .wrapContentSize()
//                ) {
//                    Column(
//                        modifier = Modifier.padding(8.dp),
//                        verticalArrangement = Arrangement.spacedBy(4.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(text = snackbarData.message)
//                    }
//                }
//            }
//        )
    }
}