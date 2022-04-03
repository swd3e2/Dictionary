package com.dictionary.presentation.category_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dictionary.presentation.category_list.CategoryListEvent

@Composable
fun AddCategoryDialog(
    categoryTitle: MutableState<String>,
    onEvent: (CategoryListEvent) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onEvent(CategoryListEvent.OnCloseAddCategoryDialog)
        },
        content = {
            Box{
                Card(modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp, 20.dp, 10.dp, 10.dp)
                    ) {
                        Text(
                            text = "Creating new category",
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = categoryTitle.value,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = MaterialTheme.typography.body2.fontSize
                            ),
                            onValueChange = {
                                onEvent(CategoryListEvent.OnChangeTitle(it))
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            label = { Text(text = "Category name") }
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { onEvent(CategoryListEvent.OnCloseAddCategoryDialog) }) {
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