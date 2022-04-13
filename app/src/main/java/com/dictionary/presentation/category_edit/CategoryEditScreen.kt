package com.dictionary.presentation.category_edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.domain.entity.Category
import com.dictionary.presentation.category_edit.components.AddWordDialog
import com.dictionary.presentation.category_edit.components.DropDownMenu
import com.dictionary.presentation.category_edit.components.WordListItem
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
                Title(category = viewModel.category, wordsCount = viewModel.wordsCount)
                GameButtons(category = viewModel.category, onEvent = viewModel::onEvent)
                SearchAndAdd(viewModel.termSearch, onEvent = viewModel::onEvent)
            }
            items(words.value) { word ->
                WordListItem(word, viewModel::onEvent)
            }
        }
    }
}

@Composable
fun Title(
    category: Category,
    wordsCount: MutableState<Int>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = category.name,
            fontSize = 30.sp,
            color = PrimaryTextColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(0.dp, 20.dp),
            text = "${wordsCount.value} terms",
            fontSize = 12.sp,
            color = PrimaryTextColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GameButtons(
    category: Category,
    onEvent: (CategoryEditEvent) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 15.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 20.dp, 15.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val cardWidth = screenWidth / 2 - 10.dp
        Card(
            modifier = Modifier
                .width(cardWidth)
                .clickable {
                    onEvent(CategoryEditEvent.OnLearnClick(category.id))
                },
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "LEARN", fontSize = 14.sp)
            }
        }
        Card(
            modifier = Modifier
                .width(cardWidth)
                .clickable {
                    onEvent(CategoryEditEvent.OnGameClick(category.id))
                },
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "CARDS", fontSize = 14.sp)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val cardWidth = screenWidth / 3 - 9.dp
        Card(
            modifier = Modifier
                .width(cardWidth)
                .clickable {
                    onEvent(CategoryEditEvent.OnLearnClick(category.id))
                },
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "MATCH", fontSize = 14.sp)
            }
        }
        Card(
            modifier = Modifier
                .width(cardWidth)
                .clickable {
                    onEvent(CategoryEditEvent.OnGameClick(category.id))
                },
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "TEST", fontSize = 14.sp)
            }
        }
        Card(
            modifier = Modifier
                .width(cardWidth)
                .clickable {
                    onEvent(CategoryEditEvent.OnGameClick(category.id))
                },
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "WRITE", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SearchAndAdd(
    search: MutableState<String>,
    onEvent: (CategoryEditEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 5.dp, 20.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(value = search.value, onValueChange = { onEvent(CategoryEditEvent.OnSearchTermChange(it)) }, label = { Text(text = "Search") })
        IconButton(
            onClick = { onEvent(CategoryEditEvent.OnOpenAddWordDialog) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colors.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}