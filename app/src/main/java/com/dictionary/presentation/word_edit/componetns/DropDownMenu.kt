package com.dictionary.presentation.word_edit.componetns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dictionary.presentation.word_edit.WordEditEvent
import com.dictionary.presentation.word_edit.WordEditViewModel

@Composable
fun DropDownMenu(
    viewModel: WordEditViewModel,
) {
    Column {
        TopAppBar(backgroundColor = Color(0x00000000), elevation = 0.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = { viewModel.onEvent(WordEditEvent.GoBack) }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colors.primary
                    )
                }
                Row {
                    if (viewModel.word != null) {
                        if (viewModel.editState.value) {
                            IconButton(
                                onClick = { viewModel.onEvent(WordEditEvent.OnView) }
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "View",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { viewModel.onEvent(WordEditEvent.OnEdit) }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                        IconButton(
                            onClick = { viewModel.onEvent(WordEditEvent.OnShowWordDeleteDialog) }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}