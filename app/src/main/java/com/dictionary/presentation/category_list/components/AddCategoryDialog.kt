package com.dictionary.presentation.category_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.presentation.category_list.CategoryListEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddCategoryDialog(
    categoryTitle: MutableState<String>,
    onEvent: (CategoryListEvent) -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onEvent(CategoryListEvent.OnHideAddCategoryDialog) },
        content = {
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                Card(
                    modifier = Modifier.wrapContentHeight(),
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp, 20.dp, 10.dp, 10.dp)
                    ) {
                        Text(
                            text = "Creating new category",
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        TextField(
                            label = { Text(text = "Definition") },
                            modifier = Modifier.fillMaxWidth(),
                            value = categoryTitle.value,
                            onValueChange = { onEvent(CategoryListEvent.OnChangeTitle(it)) },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                            ),
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { onEvent(CategoryListEvent.OnHideAddCategoryDialog) }) {
                                Text(text = "Cancel")
                            }
                            Button(onClick = {
                                onEvent(CategoryListEvent.OnSaveClick)
                            }) {
                                Text(text = "Save")
                            }
                        }
                    }
                }
            }
        }
    )
}