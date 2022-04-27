package com.dictionary.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dictionary.presentation.components.BottomBar
import com.dictionary.presentation.settings.components.DropDownMenu
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val expanded = remember { mutableStateOf(false) }
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = { DropDownMenu(onPopBackStack) },
        bottomBar = {
            BottomBar(navController, true)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .padding(30.dp, 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Count of learn words")
                ExposedDropdownMenuBox(
                    modifier = Modifier.width(120.dp),
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = viewModel.countWordsToLearn.value.toString(),
                        onValueChange = { },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded.value
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = {
                            expanded.value = false
                        }
                    ) {
                        mutableListOf(6, 12, 18).forEach {
                            DropdownMenuItem(onClick = {
                                expanded.value = !expanded.value
                                viewModel.onEvent(SettingsEvent.OnChangeCountOfWordsToLearn(it))
                            }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(30.dp, 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Dark theme")
                Switch(
                    modifier = Modifier.offset { IntOffset(0, -40) },
                    checked = viewModel.darkTheme.value,
                    onCheckedChange = {
                        viewModel.onEvent(SettingsEvent.OnChangeDarkTheme)
                    }
                )
            }
        }
    }
}