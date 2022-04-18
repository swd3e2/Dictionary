package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.dictionary.presentation.category_edit.CategoryEditEvent

@Composable
fun DropDownMenu(
    onPopBackStack: () -> Unit,
    menuExpanded: MutableState<Boolean>,
    onEvent: (CategoryEditEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column {
        TopAppBar(backgroundColor = Color(0x00000000), elevation = 0.dp){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        onPopBackStack()
                    }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colors.primary)
                }
                Box{
                    IconButton(onClick = {
                        onEvent(CategoryEditEvent.OnMenuClick)
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Show menu")
                    }
                    DropdownMenu(expanded = menuExpanded.value, onDismissRequest = {
                        onEvent(CategoryEditEvent.OnCloseMenu)
                    }) {
                        DropdownMenuItem(onClick = { onEvent(CategoryEditEvent.OnShowDeleteCategoryDialog) }) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }
    }
}