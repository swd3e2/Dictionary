package com.dictionary.presentation.category_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.presentation.category_list.components.AddCategoryDialog
import com.dictionary.presentation.category_list.components.CategoryListItem
import com.dictionary.presentation.components.DeleteDialog
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor
import com.dictionary.utils.UiEvent

@Composable
fun CategoryListScreen(
    launchFileIntent: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: CategoriesListViewModel = hiltViewModel()
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
                        Text(
                            text = "Your categories",
                            fontSize = 30.sp,
                            color = PrimaryTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = launchFileIntent
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Import",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
                items(categories.value) { category ->
                    CategoryListItem(category, viewModel::onEvent)
                }
            }
        }
    }
}