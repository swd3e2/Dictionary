package com.dictionary.presentation.category_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.presentation.category_edit.components.WordListItem
import com.dictionary.presentation.category_list.components.AddCategoryDialog
import com.dictionary.presentation.category_list.components.CategoryListItem
import com.dictionary.presentation.common.BottomBar
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
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomBar()
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .wrapContentHeight()
            ) {
                if (viewModel.openDialog.value) {
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
                            IconButton(
                                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                                onClick = {
                                    viewModel.onEvent(CategoryListEvent.OnOpenAddCategoryDialog)
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                    }
                    items(categories.value) { category ->
                        CategoryListItem(category, viewModel::onEvent)
                    }
                }
//                    for (category in viewModel.categories) {
//                        CategoryListItem(
//                            category = category,
//                            onEvent = viewModel::onEvent
//                        )
//                    }
            }
        }
    }
}