package com.dictionary.presentation.word_edit.componetns

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.presentation.category_edit.WordTranslationState
import com.dictionary.presentation.category_list.CategoryListEvent
import com.dictionary.presentation.common.DisabledInteractionSource
import com.dictionary.presentation.word_edit.WordEditEvent
import com.dictionary.ui.theme.SecondaryTextColor
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TranslationDialog(
    translationState: State<WordTranslationState>,
    selectedTranslations: SnapshotStateList<String>,
    onEvent: (WordEditEvent) -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            onEvent(WordEditEvent.OnHideTranslationDialog)
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
                            .padding(15.dp)
                            .wrapContentHeight()
                    ) {
                        if (translationState.value.isLoading) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        } else {
                            translationState.value.translation?.let {
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
                                        for (trans in word.translation) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .background(
                                                        color = if (MaterialTheme.colors.isLight) {
                                                            if (!selectedTranslations.contains(trans))
                                                                Color(0xffE1ECFB)
                                                            else MaterialTheme.colors.primary
                                                        } else {
                                                            if (!selectedTranslations.contains(trans))
                                                                Color(0xFF6E6D79)
                                                            else MaterialTheme.colors.primary
                                                        },
                                                        shape = RoundedCornerShape(50)
                                                    )
                                                    .clickable(
                                                        interactionSource = DisabledInteractionSource(),
                                                        null,
                                                        onClick = {
                                                            onEvent(
                                                                WordEditEvent.OnClickOnTranslation(
                                                                    trans
                                                                )
                                                            )
                                                        }
                                                    )
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(8.dp, 3.dp),
                                                    text = trans,
                                                    color = if (!selectedTranslations.contains(trans))
                                                        MaterialTheme.colors.onBackground
                                                    else MaterialTheme.colors.onPrimary,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.padding(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(onClick = { onEvent(WordEditEvent.OnHideTranslationDialog) }) {
                                    Text(text = "Cancel")
                                }
                                Button(onClick = { onEvent(WordEditEvent.OnApplyTranslation) }) {
                                    Text(text = "OK")
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}