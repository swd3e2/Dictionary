package com.dictionary.presentation.category_edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dictionary.R
import com.dictionary.domain.entity.Category
import com.dictionary.presentation.category_edit.components.*
import com.dictionary.presentation.common.lifecycle_observer.GetImageLifecycleObserver
import com.dictionary.presentation.components.DeleteDialog
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoryEditScreen(
    getImageLifecycleObserver: GetImageLifecycleObserver,
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: CategoryEditViewModel = hiltViewModel()
) {
    getImageLifecycleObserver.reset()

    val words = viewModel.wordsState.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                UiEvent.PopBackStack -> onPopBackStack()
            }
        }
    }
    LaunchedEffect(key1 = true) {
        getImageLifecycleObserver.filenameStateFlow.collectLatest {
            it?.let {
                viewModel.onEvent(CategoryEditEvent.OnImagePickFile(it))
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            DropDownMenu(
                onPopBackStack,
                viewModel.menuExpanded,
                viewModel::onEvent
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(CategoryEditEvent.OnAddWord)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        if (viewModel.showWordDeleteDialog.value) {
            DeleteDialog(
                text = "Are you sure you want to delete word?",
                onClose = { viewModel.onEvent(CategoryEditEvent.OnHideWordDeleteDialog) },
                onSuccess = { viewModel.onEvent(CategoryEditEvent.OnDeleteWord) }
            )
        }

        if (viewModel.showMoveToCategoryDialog.value) {
            MoveToCategoryDialog(
                viewModel::onEvent,
                viewModel.categories,
            )
        }
        if (viewModel.showCategoryDeleteDialog.value) {
            DeleteDialog(
                text = "Are you sure you want to delete category?",
                onClose = { viewModel.onEvent(CategoryEditEvent.OnHideDeleteCategoryDialog) },
                onSuccess = { viewModel.onEvent(CategoryEditEvent.OnDeleteCategory)}
            )
        }

        if (viewModel.showSortDialog.value) {
            SortDialog(
                viewModel::onEvent,
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Title(
                    getImageLifecycleObserver = getImageLifecycleObserver,
                    category = viewModel.category,
                    wordsCount = viewModel.wordsCount,
                    categoryTitle = viewModel.categoryTitle,
                    categoryImage = viewModel.categoryImage,
                    onEvent = viewModel::onEvent
                )
                GameButtons(category = viewModel.category, onEvent = viewModel::onEvent)
                SearchAndSort(viewModel.termSearch, onEvent = viewModel::onEvent)
            }
            items(words.value) { word ->
                key(word.id) {
                    WordListItem(word, viewModel::onEvent, canMoveWordToCategory = viewModel.categories.isNotEmpty())
                }
            }
        }
    }
}

@Composable
fun Title(
    getImageLifecycleObserver: GetImageLifecycleObserver,
    category: Category,
    categoryTitle: MutableState<String>,
    categoryImage: MutableState<String>,
    wordsCount: MutableState<Int>,
    onEvent: (CategoryEditEvent) -> Unit
) {
    val showTextField = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                if (categoryImage.value.isNotEmpty()) {
                    AsyncImage(
                        model = categoryImage.value,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(30))
                            .clickable {
                                getImageLifecycleObserver.selectImage()
                            },
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .clickable {
                                getImageLifecycleObserver.selectImage()
                            },
                        contentScale = ContentScale.Crop,
                        painter = painterResource(R.drawable.placeholder),
                        contentDescription = "Contact profile picture",
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                if (!showTextField.value) {
                    Text(
                        text = category.name,
                        fontSize = 30.sp,
                        color = PrimaryTextColor,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        modifier = Modifier.offset((-4).dp, (-18).dp),
                        onClick = { showTextField.value = !showTextField.value }
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    TextField(
                        label = { Text(text = "Title") },
                        modifier = Modifier.fillMaxWidth(),
                        value = categoryTitle.value,
                        onValueChange = { onEvent(CategoryEditEvent.OnTitleChange(it)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onEvent(CategoryEditEvent.OnTitleSave)
                                showTextField.value = !showTextField.value
                                focusManager.clearFocus()
                            }
                        ),
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    showTextField.value = !showTextField.value
                                }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Edit",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onEvent(CategoryEditEvent.OnTitleSave)
                                    showTextField.value = !showTextField.value
                                    focusManager.clearFocus()
                                }) {
                                Icon(
                                    Icons.Default.Done,
                                    contentDescription = "Edit",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                        ),
                    )
                }
            }
        }
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
                Text(text = "REPEAT", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SearchAndSort(
    search: MutableState<String>,
    onEvent: (CategoryEditEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = search.value,
            onValueChange = { onEvent(CategoryEditEvent.OnSearchTermChange(it)) },
            label = { Text(text = "Search") },
            modifier = Modifier
                .padding(15.dp, 0.dp, 0.dp, 0.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
        )
        IconButton(
            modifier = Modifier.padding(15.dp),
            onClick = { onEvent(CategoryEditEvent.OnShowSortDialog) }
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Edit",
                tint = MaterialTheme.colors.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}