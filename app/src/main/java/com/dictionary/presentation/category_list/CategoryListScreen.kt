package com.dictionary.presentation.category_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.presentation.category_list.components.AddCategoryDialog
import com.dictionary.presentation.category_list.components.CategoryListItem
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor
import com.dictionary.utils.UiEvent

@Composable
fun CategoryListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: CategoriesListViewModel = hiltViewModel()
) {
    val categories = viewModel.categories.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.onEvent(CategoryListEvent.OnOpenAddCategoryDialog)
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        ) {
            Column {
                Column(modifier = Modifier.fillMaxWidth().padding(30.dp)) {
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
                if (viewModel.openDialog.value) {
                    AddCategoryDialog(
                        viewModel.title,
                        viewModel::onEvent
                    )
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(0.dp, 5.dp)
                            .wrapContentHeight()
                    ) {
                        items(categories.value) { category ->
                            CategoryListItem(
                                category = category,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}