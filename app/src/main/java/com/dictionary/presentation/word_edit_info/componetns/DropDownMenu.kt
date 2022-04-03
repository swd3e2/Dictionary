package com.dictionary.presentation.word_edit_info.componetns

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.dictionary.presentation.word_edit_info.WordEditEvent

@Composable
fun DropDownMenu(
    label: String,
    onPopBackStack: () -> Unit,
    menuExpanded: MutableState<Boolean>,
    onEvent: (WordEditEvent) -> Unit
) {
    Column {
        TopAppBar() {
            IconButton(
                onClick = {
                    onPopBackStack()
                }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = label)
            IconButton(onClick = {
                onEvent(WordEditEvent.OnMenuClick)
            }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Show menu")
            }
            DropdownMenu(expanded = menuExpanded.value, onDismissRequest = {
                onEvent(WordEditEvent.OnCloseMenu)
            }) {
                DropdownMenuItem(onClick = { }) {
                    Text(text = "Settings")
                }
                DropdownMenuItem(onClick = { }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}