package com.dictionary.presentation.word_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.presentation.components.DeleteDialog
import com.dictionary.presentation.word_edit.componetns.DropDownMenu
import com.dictionary.presentation.word_edit.componetns.TranslationDialog
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun WordEditScreen(
    onPopBackStack: () -> Unit,
    viewModel: WordEditViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    focusManager.clearFocus()
                    onPopBackStack()
                }
                is UiEvent.ShowSnackbar -> {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }
                UiEvent.ClearFocus -> focusManager.clearFocus()
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
            if (viewModel.editState.value) {
                FloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = { viewModel.onEvent(WordEditEvent.OnSaveClick) },
                    contentColor = MaterialTheme.colors.secondary,
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(40)
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Add")
                }
            }
        },
        topBar = {
            DropDownMenu(viewModel)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (viewModel.showTranslationDialog.value) {
                TranslationDialog(
                    translationState = viewModel.state,
                    selectedTranslations = viewModel.selectedTranslations,
                    onEvent = viewModel::onEvent
                )
            }
            if (viewModel.showWordDeleteDialog.value) {
                DeleteDialog(
                    text = "Are you sure you want to delete word?",
                    onClose = { viewModel.onEvent(WordEditEvent.OnHideWordDeleteDialog) },
                    onSuccess = { viewModel.onEvent(WordEditEvent.OnDeleteWord) }
                )
            }
            if (viewModel.editState.value) {
                EditFields(viewModel = viewModel)
            } else {
                ViewFields(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ViewFields(viewModel: WordEditViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp, 10.dp)) {
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        viewModel.word?.let { word ->
            Text(text = "Term", fontSize = 18.sp)
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 5.dp, 0.dp), text = word.term)
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Definition", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = word.definition
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Transcription", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = word.transcription
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Created", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = LocalDateTime.ofInstant(word.created.toInstant(), ZoneOffset.systemDefault())
                    .format(dateFormat)
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "First learned", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = if (word.firstLearned != null) LocalDateTime.ofInstant(
                    word.firstLearned!!.toInstant(),
                    ZoneOffset.systemDefault()
                ).format(dateFormat) else ""
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Last repeated", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = if (word.lastRepeated != null) LocalDateTime.ofInstant(
                    word.lastRepeated!!.toInstant(),
                    ZoneOffset.systemDefault()
                ).format(dateFormat) else ""
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Bucket", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = word.bucket.toString()
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Synonyms", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = word.synonyms
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Antonyms", fontSize = 18.sp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 0.dp),
                text = word.antonyms
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 5.dp)
            )
            Text(text = "Similar", fontSize = 18.sp)
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 5.dp, 0.dp), text = word.similar)
        }
    }
}

@Composable
fun EditFields(viewModel: WordEditViewModel) {
    val focusManager = LocalFocusManager.current

    Column(Modifier.padding(10.dp)) {
        TextField(
            value = viewModel.newTerm.value,
            onValueChange = { viewModel.onEvent(WordEditEvent.OnTermChange(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 5.dp),
            label = { Text(text = "Term") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
        )
        if (viewModel.wordWithTermExistsInCategory.value.isNotEmpty()) {
            Text(
                text = """Word already exists in category "${viewModel.wordWithTermExistsInCategory.value}" """,
                color = MaterialTheme.colors.error
            )
        }
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
            label = { Text(text = "Definition") },
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
            label = { Text(text = "Antonyms") },
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
            label = { Text(text = "Synonyms") },
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
            label = { Text(text = "Similar") },
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
            label = { Text(text = "Transcription") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
        )
    }
}