package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.flowlayout.FlowRow
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.presentation.category_edit.CategoryEditViewModel
import com.dictionary.presentation.category_edit.WordTranslationState
import com.dictionary.ui.theme.SecondaryTextColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SortDialog(
    onEvent: (CategoryEditEvent) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onEvent(CategoryEditEvent.OnHideSortDialog)
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
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(CategoryEditEvent.OnSortChange("term", "asc"))
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            ) {
                                Text(text = "Alphabetical")
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Down"
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(CategoryEditEvent.OnSortChange("term", "desc"))
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(15.dp)
                            ) {
                                Text(text = "Alphabetical")
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Up"
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(CategoryEditEvent.OnSortChange("created", "asc"))
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(15.dp)
                            ) {
                                Text(text = "Created")
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Down"
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(CategoryEditEvent.OnSortChange("created", "desc"))
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(15.dp)
                            ) {
                                Text(text = "Created")
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Up"
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(CategoryEditEvent.OnSortChange("last_repeated", "asc"))
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(15.dp)
                            ) {
                                Text(text = "Last repeated")
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Down"
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(CategoryEditEvent.OnSortChange("last_repeated", "desc"))
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(15.dp)
                            ) {
                                Text(text = "Last repeated")
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Up"
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}