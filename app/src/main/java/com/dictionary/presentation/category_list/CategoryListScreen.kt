package com.dictionary.presentation.category_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.domain.entity.Category
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.presentation.category_list.components.AddCategoryDialog
import com.dictionary.presentation.category_list.components.CategoryListItem
import com.dictionary.presentation.common.GetFile
import com.dictionary.presentation.components.DeleteDialog
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoryListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: CategoriesListViewModel = hiltViewModel(),
    getFile: GetFile
) {


    val scaffoldState = rememberScaffoldState()
    val categories = viewModel.categories.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    LaunchedEffect(key1 = true) {
        getFile.filenameStateFlow.collectLatest {
            it?.let { viewModel.onEvent(CategoryListEvent.OnImportFile(it)) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(CategoryListEvent.OnShowAddCategoryDialog)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        if (viewModel.showDeleteDialog.value) {
            DeleteDialog(
                text = "Are you sure you want to delete category?",
                onClose = { viewModel.onEvent(CategoryListEvent.OnHideDeleteDialog) },
                onSuccess = { viewModel.onEvent(CategoryListEvent.OnDeleteCategory)}
            )
        }
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentHeight()
        ) {
            if (viewModel.showAddCategoryDialog.value) {
                AddCategoryDialog(
                    viewModel.title,
                    viewModel::onEvent
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Title()
                    GameButtons(viewModel::onEvent)
                }
                item {
                    Search(viewModel::onEvent, viewModel.search, getFile)
                }
                items(categories.value) { category ->
                    CategoryListItem(category, viewModel::onEvent)
                }
                item{
                    Spacer(modifier = Modifier.padding(25.dp))
                }
            }
        }
    }
}

@Composable
private fun Search(
    onEvent: (CategoryListEvent) -> Unit,
    search: MutableState<String>,
    getFile: GetFile
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(15.dp)
        ){
            Text(text = "Categories", fontSize = 28.sp, color = PrimaryTextColor, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = search.value,
                onValueChange = {
                    onEvent(CategoryListEvent.OnSearchChange(it))
                },
                label = { Text(text = "Search") },
                modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                ),
            )
            IconButton(
                modifier = Modifier.padding(15.dp),
                onClick = getFile::selectFile
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Import",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
private fun Title() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp, 30.dp, 30.dp, 0.dp),
    ) {
        Text(
            text = "Welcome home, Master",
            color = SecondaryTextColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GameButtons(
    onEvent: (CategoryListEvent) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 15.dp
    Row(
        modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 5.dp)
    ){
        Text(text = "Games", fontSize = 28.sp, color = PrimaryTextColor, fontWeight = FontWeight.Bold)
    }
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
                    onEvent(CategoryListEvent.OnGoToLearnWords)
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
                    onEvent(CategoryListEvent.OnGoToCardsGame)
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
                Text(text = "REPEAT", fontSize = 14.sp)
            }
        }
    }
}