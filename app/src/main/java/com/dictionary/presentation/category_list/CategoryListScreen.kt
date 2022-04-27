package com.dictionary.presentation.category_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dictionary.presentation.category_list.components.AddCategoryDialog
import com.dictionary.presentation.category_list.components.CategoryListItem
import com.dictionary.presentation.common.lifecycle_observer.GetFileLifecycleObserver
import com.dictionary.presentation.components.BottomBar
import com.dictionary.presentation.components.DeleteDialog
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CategoryListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    navController: NavHostController,
    viewModel: CategoriesListViewModel = hiltViewModel(),
    getFileLifecycleObserver: GetFileLifecycleObserver
) {
    getFileLifecycleObserver.reset()
    LaunchedEffect(Unit) {
        viewModel.load()
    }
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
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

    LaunchedEffect(key1 = true) {
        getFileLifecycleObserver.filenameStateFlow.collectLatest {
            it?.let {
                viewModel.onEvent(CategoryListEvent.OnImportFile(it))
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding(),
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(CategoryListEvent.OnShowAddCategoryDialog) },
                contentColor = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(40)
            ) {
                Icon(
                    Icons.Filled.Add, ""
                )
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
        if (viewModel.showDeleteCategoryDialog.value) {
            DeleteDialog(
                text = "Are you sure you want to delete category?",
                onClose = { viewModel.onEvent(CategoryListEvent.OnHideDeleteCategoryDialog) },
                onSuccess = { viewModel.onEvent(CategoryListEvent.OnDeleteCategory)}
            )
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .wrapContentHeight()
                .imePadding()
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
                    Buttons(viewModel::onEvent, getFileLifecycleObserver)
                }
                item {
                    if (viewModel.isLoading.value) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                items(viewModel.categories) { category ->
                    CategoryListItem(
                        category,
                        viewModel.countByCategory,
                        viewModel.countToLearn,
                        viewModel.countToRepeat,
                        viewModel::onEvent
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(15.dp))
                }
            }
        }
    }
}

@Composable
private fun Buttons(
    onEvent: (CategoryListEvent) -> Unit,
    getFileLifecycleObserver: GetFileLifecycleObserver
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "Categories",
                fontSize = 24.sp,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                modifier = Modifier.padding(0.dp, 15.dp, 6.dp, 15.dp),
                shape = RoundedCornerShape(50f),
                onClick = { onEvent(CategoryListEvent.OnExportFile) }
            ) {
                Text(text = "Export")
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Export",
                    tint = MaterialTheme.colors.surface
                )
            }
            Button(
                modifier = Modifier.padding(0.dp, 15.dp, 15.dp, 15.dp),
                shape = RoundedCornerShape(50f),
                onClick = getFileLifecycleObserver::selectFile
            ) {
                Text(text = "Import")
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Import",
                    tint = MaterialTheme.colors.surface
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
            .padding(15.dp, 30.dp, 30.dp, 0.dp),
    ) {
        Text(
            text = "Welcome home, Master",
            color = MaterialTheme.colors.onBackground,
            fontSize = 28.sp,
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
    ) {
        Text(
            text = "Games",
            fontSize = 24.sp,
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Bold
        )
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
            elevation = 4.dp
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
            elevation = 4.dp
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