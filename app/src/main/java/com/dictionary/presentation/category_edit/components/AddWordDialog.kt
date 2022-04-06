package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
fun AddWordDialog(
    viewModel: CategoryEditViewModel,
    wordTerm: MutableState<String>,
    wordDefinition: MutableState<String>,
    wordWithTermExists: MutableState<Boolean>,
    wordTranslationState: State<WordTranslationState>,
    onEvent: (CategoryEditEvent) -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            onEvent(CategoryEditEvent.OnCloseAddWordDialog)
        },
        content = {
            Box {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .width(300.dp),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp, 20.dp, 10.dp, 10.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Creating new word",
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        CustomTextField(
                            "Term",
                            wordTerm,
                            { onEvent(CategoryEditEvent.OnTermChange(it)) },
                            { onEvent(CategoryEditEvent.OnTermChange("")) },
                        )
                        if (wordWithTermExists.value) {
                            Text(text = "Word already exists", color = Color(0xFFCF3A3F))
                        }
                        CustomTextField(
                            "Definition",
                            wordDefinition,
                            { onEvent(CategoryEditEvent.OnDefinitionChange(it)) },
                            { onEvent(CategoryEditEvent.OnDefinitionChange("")) },
                        )
                        if (wordTranslationState.value.isLoading) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                        viewModel.state.value.translation?.let {
                            for (word in it.words) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = word.word,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.padding(2.dp, 0.dp))
                                    Text(
                                        text = "[${word.type}]",
                                        color = SecondaryTextColor,
                                        fontSize = 10.sp,
                                    )
                                }
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                ) {
                                    for (translation in word.translation) {
                                        Box(modifier = Modifier.padding(2.dp)) {
                                            Text(
                                                modifier = Modifier
                                                    .background(
                                                        color = if (!viewModel.wordTranslations.contains(
                                                                translation
                                                            )
                                                        )
                                                            Color(0xffE1ECFB) else Color(0xFF87AAD5),
                                                        shape = RoundedCornerShape(50)
                                                    )
                                                    .clip(RoundedCornerShape(50))
                                                    .clickable {
                                                        if (!viewModel.wordTranslations.contains(
                                                                translation
                                                            )
                                                        ) {
                                                            viewModel.wordTranslations.add(
                                                                translation
                                                            )
                                                        } else {
                                                            viewModel.wordTranslations.remove(
                                                                translation
                                                            )
                                                        }
                                                    },
                                                text = translation,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = { onEvent(CategoryEditEvent.GetTranslation) }) {
                            Text(text = "Translate")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { onEvent(CategoryEditEvent.OnCloseAddWordDialog) }) {
                                Text(text = "Cancel")
                            }
                            Button(onClick = {
                                onEvent(CategoryEditEvent.OnWordSaveClick)
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