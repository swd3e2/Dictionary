package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.category_edit.CategoryEditEvent
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WordListItem(
    word: Word,
    onEvent: (CategoryEditEvent) -> Unit,
    canMoveWordToCategory: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val squareSize = if (word.bucket > 1) (-140).dp else (-100).dp
    val swipeAbleState = SwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    Box(
        modifier = Modifier.padding(15.dp, 5.dp).height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (word.bucket > 1) {
                IconButton(
                    onClick = {
                        onEvent(CategoryEditEvent.OnDropWordBucket(word))
                        coroutineScope.launch { swipeAbleState.snapTo(0) }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Refresh, "Refresh")
                }
            }
            if (canMoveWordToCategory) {
                IconButton(
                    onClick = { onEvent(CategoryEditEvent.OnShowMoveToCategoryDialog(word)) }
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, "Delete")
                }
            }
            IconButton(
                onClick = { onEvent(CategoryEditEvent.OnShowWordDeleteDialog(word)) }
            ) {
                Icon(imageVector = Icons.Default.Delete, "Delete")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset( swipeAbleState.offset.value.roundToInt(), 0 ) }
                .swipeable(
                    state = swipeAbleState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            Card(
                Modifier
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { onEvent(CategoryEditEvent.OnWordClick(word.id)) }
                    .fillMaxSize(),
                elevation = 2.dp
            ) {
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    if (word.bucket > 0) {
                        VerticalProgress(
                            modifier = Modifier.fillMaxHeight().width(10.dp),
                            color = MaterialTheme.colors.primary,
                            progress = word.bucket.toFloat() / 8f
                        )
                    } else {
                        VerticalProgress(
                            modifier = Modifier.fillMaxHeight().width(10.dp),
                            color = if (MaterialTheme.colors.isLight) Color(0xFF3EAF20) else Color(0xFF81B977),
                            progress = 1f
                        )
                    }
                    Column (modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = word.term,
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colors.primary
                        )
                        Text(
                            text = word.definition,
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Created: ${LocalDateTime.ofInstant(word.created.toInstant(), ZoneOffset.systemDefault()).format(dateFormat)}",
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 12.sp
                        )
                        if (word.firstLearned != null) {
                            Text(
                                text = "First learned: ${LocalDateTime.ofInstant(word.firstLearned!!.toInstant(), ZoneOffset.systemDefault()).format(dateFormat)}",
                                style = MaterialTheme.typography.body1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 12.sp
                            )
                        }
                        if (word.lastRepeated != null) {
                            Text(
                                text = "Last repeated: ${LocalDateTime.ofInstant(word.lastRepeated!!.toInstant(), ZoneOffset.systemDefault()).format(dateFormat)}",
                                style = MaterialTheme.typography.body1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 12.sp
                            )
                        }
                        if (word.synonyms.isNotEmpty()) {
                            Text(
                                text = "Synonyms: ${word.synonyms}",
                                style = MaterialTheme.typography.body1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 12.sp
                            )
                        }
                        if (word.antonyms.isNotEmpty()) {
                            Text(
                                text = "Antonyms: ${word.antonyms}",
                                style = MaterialTheme.typography.body1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 12.sp
                            )
                        }
                        if (word.similar.isNotEmpty()) {
                            Text(
                                text = "Similar: ${word.similar}",
                                style = MaterialTheme.typography.body1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun VerticalProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color
) {
    Column(
        modifier = modifier
            .width(16.dp)
    ) {
        if (progress < 1f) {
            Box(
                modifier = Modifier
                    .weight((if ((1 - progress) == 0f) 0.0001 else 1 - progress) as Float)
                    .fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .weight(progress)
                .fillMaxWidth()
                .background(
                    color = color
                )
        )
    }
}