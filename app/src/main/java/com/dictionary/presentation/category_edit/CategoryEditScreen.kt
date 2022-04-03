package com.dictionary.presentation.category_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.category_edit.components.AddWordDialog
import com.dictionary.presentation.category_edit.components.DropDownMenu
import com.dictionary.presentation.category_edit.components.WordListItem
import com.dictionary.utils.UiEvent

@Composable
fun CategoryEditScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: CategoryEditViewModel = hiltViewModel()
) {
    val words = viewModel.words?.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(CategoryEditEvent.OnOpenAddWordDialog)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            DropDownMenu(viewModel.category.name, onPopBackStack, viewModel.menuExpanded, viewModel::onEvent)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (viewModel.openDialog.value) {
                AddWordDialog(
                    viewModel,
                    viewModel.newWordTerm,
                    viewModel.newWordDefinition,
                    viewModel.state,
                    viewModel::onEvent,
                )
            }

            LazyColumn {
                words?.let {
                    items(words.value) { word ->
                        WordListItem(word, viewModel::onEvent)
                    }
                }
            }
        }
    }
}
