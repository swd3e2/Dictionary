package com.dictionary.presentation.category_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dictionary.domain.entity.Category
import com.dictionary.presentation.category_list.CategoryListEvent
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryListItem(
    category: Category,
    countMap: SnapshotStateMap<Int, Int>,
    countToLearnMap: SnapshotStateMap<Int, Int>,
    countToRepeatMap: SnapshotStateMap<Int, Int>,
    onEvent: (CategoryListEvent) -> Unit
) {
    val countToRepeat = countToRepeatMap[category.id]
    val countToLearn = countToLearnMap[category.id]
    val count = countMap[category.id]
    val squareSize = (-60).dp
    val swipeAbleState = SwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    Box(
        modifier = Modifier
            .padding(15.dp, 5.dp)
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEvent(CategoryListEvent.OnShowDeleteCategoryDialog(category)) }
            ) {
                Icon(imageVector = Icons.Default.Delete, "Delete")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeAbleState.offset.value.roundToInt(), 0) }
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
                    .clickable { onEvent(CategoryListEvent.OnCategoryClick(category.id)) }
                    .fillMaxSize()
                    .height(IntrinsicSize.Min),
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (countToLearn != null && countToLearn > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(5.dp)
                                .background(
                                    color = if (MaterialTheme.colors.isLight) Color(
                                        0xFF3EAF20
                                    ) else Color(0xFF81B977)
                                )
                        )
                    } else if (countToRepeat != null && countToRepeat > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(5.dp)
                                .background(color = MaterialTheme.colors.primary)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxHeight()
                    ) {
                        if (category.image.isNotEmpty()) {
                            AsyncImage(
                                model = category.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(30)),
                            )
                        }
                        Column(
                            modifier = Modifier.padding(5.dp, 0.dp)
                        ) {
                            Row {
                                Column(
                                    modifier = Modifier.padding(15.dp, 0.dp, 0.dp)
                                ) {
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.body1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp),
                                        text = "${count ?: 0} words",
                                        style = MaterialTheme.typography.body1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    if (countToLearn != null && countToLearn > 0) {
                                        Box(
                                            modifier = Modifier
                                                .wrapContentHeight()
                                                .wrapContentWidth()
                                                .background(
                                                    color = if (MaterialTheme.colors.isLight) Color(
                                                        0xFF3EAF20
                                                    ) else Color(0xFF81B977),
                                                    shape = CircleShape
                                                )
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(8.dp, 5.dp),
                                                text = "$countToLearn",
                                                style = MaterialTheme.typography.body1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center,
                                                color = Color.White
                                            )
                                        }
                                    }
                                    if (countToRepeat != null && countToRepeat > 0) {
                                        Spacer(modifier = Modifier.padding(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .wrapContentHeight()
                                                .wrapContentWidth()
                                                .background(
                                                    color = MaterialTheme.colors.primary,
                                                    shape = CircleShape
                                                )
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(8.dp, 5.dp),
                                                text = "$countToRepeat",
                                                style = MaterialTheme.typography.body1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
