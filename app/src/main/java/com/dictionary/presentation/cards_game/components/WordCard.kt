package com.dictionary.presentation.cards_game.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.cards_game.CardsGameEvent
import com.dictionary.presentation.common.DisabledInteractionSource
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WordCard(
    onEvent: (CardsGameEvent) -> Unit,
    word: Word,
) {
    val coroutineScope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    val rotation = remember { Animatable(0f) }
    Card(
        modifier = Modifier
            .offset {
                IntOffset(
                    offset.value.x.roundToInt(),
                    offset.value.y.roundToInt()
                )
            }
            .padding(10.dp, 10.dp)
            .height(520.dp)
            .width(330.dp)
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 18f * density
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        coroutineScope.launch {
                            if (rotation.value <= 90f) {
                                offset.snapTo(
                                    Offset(
                                        offset.value.x + dragAmount.x,
                                        offset.value.y + dragAmount.y
                                    )
                                )
                            } else {
                                offset.snapTo(
                                    Offset(
                                        offset.value.x - dragAmount.x,
                                        offset.value.y + dragAmount.y
                                    )
                                )
                            }
                        }
                    },
                    onDragEnd = {
                        when {
                            offset.value.x < -260.0f -> {
                                onEvent(CardsGameEvent.WordNotLearned(word))
                                coroutineScope.launch {
                                    offset.animateTo(
                                        targetValue = Offset(-1000f, offset.value.y),
                                        animationSpec = tween(
                                            durationMillis = 150,
                                            delayMillis = 0
                                        )
                                    )
                                    if (rotation.value >= 90f) {
                                        rotation.snapTo(0f)
                                    }
                                    offset.snapTo(Offset(0f, 0f))
                                }
                            }
                            offset.value.x > 260.0f -> {
                                onEvent(CardsGameEvent.WordLearned(word))
                                coroutineScope.launch {
                                    offset.animateTo(
                                        targetValue = Offset(1000f, offset.value.y),
                                        animationSpec = tween(
                                            durationMillis = 150,
                                            delayMillis = 0
                                        )
                                    )
                                    if (rotation.value >= 90f) {
                                        rotation.snapTo(0f)
                                    }
                                    offset.snapTo(Offset(0f, 0f))
                                }
                            }
                            else -> {
                                coroutineScope.launch {
                                    offset.animateTo(
                                        targetValue = Offset(0f, 0f),
                                        animationSpec = tween(
                                            durationMillis = 150,
                                            delayMillis = 0
                                        )
                                    )
                                }
                            }
                        }
                    },
                )
            },
        onClick = {
            coroutineScope.launch {
                rotation.animateTo(
                    targetValue = if (rotation.value == 0f) 180f else 0f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                )
            }
        },
        interactionSource = remember { DisabledInteractionSource() },
        elevation = 0.dp
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (rotation.value <= 90f) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = word.term)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                rotationY = 180f
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = word.definition)
                    }
                }
            }
        }
    }
}