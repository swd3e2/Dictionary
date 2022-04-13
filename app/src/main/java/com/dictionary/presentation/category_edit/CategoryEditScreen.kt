package com.dictionary.presentation.category_edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.category_edit.components.AddWordDialog
import com.dictionary.presentation.category_edit.components.DropDownMenu
import com.dictionary.presentation.category_edit.components.WordListItem
import com.dictionary.presentation.category_list.CategoryListEvent
import com.dictionary.presentation.common.DisabledInteractionSource
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.utils.UiEvent

@Composable
fun CategoryEditScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: CategoryEditViewModel = hiltViewModel()
) {
    val words = viewModel.wordsState.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            DropDownMenu(
                viewModel.category.name,
                onPopBackStack,
                viewModel.menuExpanded,
                viewModel::onEvent
            )
        }
    ) { padding ->
        if (viewModel.openDialog.value) {
            AddWordDialog(
                viewModel,
                viewModel.newWordTerm,
                viewModel.newWordDefinition,
                viewModel.wordWithTermExists,
                viewModel.state,
                viewModel::onEvent,
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 0.dp)
                ) {
                    Text(
                        text = viewModel.category.name,
                        fontSize = 30.sp,
                        color = PrimaryTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 5.dp)
                        .clickable {
                            viewModel.onEvent(CategoryEditEvent.OnLearnClick(viewModel.category.id))
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp, 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Learn")
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Add",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 5.dp)
                        .clickable {
                            viewModel.onEvent(CategoryEditEvent.OnGameClick(viewModel.category.id))
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp, 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Card game")
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 5.dp, 20.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(value = viewModel.termSearch.value, onValueChange = { viewModel.onEvent(CategoryEditEvent.OnSearchTermChange(it)) }, label = { Text(text = "Search") })
                    IconButton(
                        onClick = { viewModel.onEvent(CategoryEditEvent.OnOpenAddWordDialog) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            items(words.value) { word ->
                WordListItem(word, viewModel::onEvent)
            }
        }
    }
}
