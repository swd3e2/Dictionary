package com.dictionary.presentation.search_words.components

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
) {
    val focusManager = LocalFocusManager.current

    Column {
        TopAppBar(backgroundColor = Color(0x00000000), elevation = 0.dp){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        onPopBackStack()
                    }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colors.primary)
                }
            }
        }
    }
}