package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dictionary.domain.entity.Category
import com.dictionary.presentation.category_edit.CategoryEditEvent

@Composable
fun MoveToCategoryDialog(
    onEvent: (CategoryEditEvent) -> Unit,
    categories: List<Category>
) {
    Dialog(
        onDismissRequest = {
            onEvent(CategoryEditEvent.OnHideMoveToCategoryDialog)
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
                    ) {
                        Text(
                            text = "Move to",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )
                        for (category in categories) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onEvent(CategoryEditEvent.OnMoveWordToCategory(category))
                                }
                            ) {
                                Text(text = category.name, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}