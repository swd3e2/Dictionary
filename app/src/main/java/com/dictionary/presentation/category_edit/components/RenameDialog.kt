package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.presentation.word_edit.WordEditEvent

@Composable
fun RenameDialog(
    onEvent: (CategoryEditEvent) -> Unit,
    categoryName: MutableState<String>,
) {
    Dialog(
        onDismissRequest = {
            onEvent(CategoryEditEvent.OnHideRenameDialog)
        },
        content = {
            Box {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .width(250.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(20.dp)
                    ) {
                        TextField(
                            label = { Text(text = "Name") },
                            modifier = Modifier.fillMaxWidth(),
                            value = categoryName.value,
                            onValueChange = { onEvent(CategoryEditEvent.OnCategoryNameChange(it)) },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        categoryName.value = ""
                                    }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = { onEvent(CategoryEditEvent.OnHideRenameDialog) }) {
                                Text(text = "Cancel")
                            }
                            Button(onClick = { onEvent(CategoryEditEvent.OnRenameCategory) }) {
                                Text(text = "Ok")
                            }
                        }
                    }
                }
            }
        }
    )

}