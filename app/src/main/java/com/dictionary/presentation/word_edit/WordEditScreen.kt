package com.dictionary.presentation.word_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.word_edit.componetns.DropDownMenu

@Composable
fun WordEditScreen(
    onPopBackStack: () -> Unit,
    viewModel: WordEditViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
//                viewModel.onEvent(CategoryEditEvent.OnWordSaveClick)
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
//            Text(text = viewModel.category.name, fontSize = 40.sp)
//            CustomTextField(
//                "Term",
//                viewModel.newWordTerm,
//                { it -> viewModel.onEvent(CategoryEditEvent.OnTermChange(it)) },
//                { viewModel.onEvent(CategoryEditEvent.OnTermChange("")) },
//            )
//            CustomTextField(
//                "Definition",
//                viewModel.newWordDefinition,
//                { it -> viewModel.onEvent(CategoryEditEvent.OnDefinitionChange(it)) },
//                { viewModel.onEvent(CategoryEditEvent.OnDefinitionChange("")) },
//            )
//            LazyColumn {
//                words?.let {
//                    items(words.value) { word ->
//                        WordListItem(word, viewModel::onEvent)
//                    }
//                }
//            }
        }
    }
}