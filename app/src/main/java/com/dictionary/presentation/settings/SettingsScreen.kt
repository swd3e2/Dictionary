package com.dictionary.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dictionary.presentation.common.Settings
import com.dictionary.presentation.components.BottomBar
import com.dictionary.presentation.settings.components.DropDownMenu
import com.dictionary.utils.UiEvent

@Composable
fun SettingsScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    focusManager.clearFocus()
                    onNavigate(event)
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
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
                modifier = Modifier.padding(30.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Dark theme")
                Switch(
                    modifier = Modifier.offset{IntOffset(0, -40)},
                    checked = viewModel.darkTheme.value,
                    onCheckedChange = {
                        viewModel.onEvent(SettingsEvent.OnChangeDarkTheme)
                    }
                )
            }
        }
    }
}