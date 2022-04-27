package com.dictionary.presentation.search_words

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dictionary.presentation.search_words.components.MoveToCategoryDialog
import com.dictionary.presentation.search_words.components.WordListItem
import com.dictionary.presentation.search_words.components.SortDialog
import com.dictionary.presentation.components.BottomBar
import com.dictionary.presentation.components.DeleteDialog
import com.dictionary.presentation.search_words.components.DropDownMenu
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.launch

@Composable
fun SearchWordsScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    navController: NavHostController,
    viewModel: SearchWordsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val words = viewModel.wordsState.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    focusManager.clearFocus()
                    onNavigate(event)
                }
                is UiEvent.ShowSnackbar -> {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = { DropDownMenu(onPopBackStack) },
        bottomBar = {
            BottomBar(navController, true)
        }
    ) { padding ->
        if (viewModel.showSortDialog.value) {
            SortDialog(
                viewModel::onEvent,
            )
        }
        if (viewModel.showWordDeleteDialog.value) {
            DeleteDialog(
                text = "Are you sure you want to delete word?",
                onClose = { viewModel.onEvent(SearchWordsEvent.OnHideWordDeleteDialog) },
                onSuccess = { viewModel.onEvent(SearchWordsEvent.OnDeleteWord) }
            )
        }
        if (viewModel.showMoveToCategoryDialog.value) {
            MoveToCategoryDialog(
                viewModel::onEvent,
                viewModel.categories,
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = viewModel.termSearch.value,
                        onValueChange = { viewModel.onEvent(SearchWordsEvent.OnSearchTermChange(it)) },
                        label = { Text(text = "Search") },
                        modifier = Modifier
                            .padding(15.dp, 0.dp, 0.dp, 0.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                        ),
                        trailingIcon = {
                            if (viewModel.termSearch.value.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        viewModel.termSearch.value = ""
                                    }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }
                    )
                    IconButton(
                        modifier = Modifier.padding(15.dp),
                        onClick = { viewModel.onEvent(SearchWordsEvent.OnShowSortDialog) }
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            items(words.value) { word ->
                key(word.id) {
                    WordListItem(
                        word = word,
                        onEvent = viewModel::onEvent,
                        images = viewModel.bitmapsMap,
                        canMoveWordToCategory = viewModel.categories.isNotEmpty()
                    )
                }
            }

        }
    }
}