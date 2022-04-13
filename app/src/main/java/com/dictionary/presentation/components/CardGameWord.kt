package com.dictionary.presentation.cards_game.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.cards_game.CardsGameEvent
import com.dictionary.presentation.common.DisabledInteractionSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WordCard(
    onLeftSwipe: () -> Unit,
    onRightSwipe: () -> Unit,
    word: Word,
) {
    val coroutineScope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val alphaKnow = remember { Animatable(0f) }
    val alphaDontKnow = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val showBorder = 150f

    LaunchedEffect(key1 = offset.value) {
        when {
            offset.value.x > showBorder -> {
                alphaKnow.snapTo(1f)
            }
            offset.value.x > 0 -> {
                alphaKnow.snapTo(offset.value.x / showBorder)
            }
            else -> {
                alphaKnow.snapTo(0f)
            }
        }
        when {
            offset.value.x < -showBorder -> {
                alphaDontKnow.snapTo(1f)
            }
            offset.value.x < 0 -> {
                alphaDontKnow.snapTo(-offset.value.x / showBorder)
            }
            else -> {
                alphaDontKnow.snapTo(0f)
            }
        }
    }

    Card(
        modifier = Modifier
            .offset {
                IntOffset(
                    offset.value.x.roundToInt(),
                    offset.value.y.roundToInt()
                )
            }
            .padding(10.dp, 10.dp)
            .height(550.dp)
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
                                coroutineScope.launch {
                                    delay(100)
                                    onLeftSwipe()
                                }
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
                                coroutineScope.launch {
                                    delay(100)
                                    onRightSwipe()
                                }
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
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    Box {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(
                                        red = 22,
                                        green = 192,
                                        blue = 84,
                                        alpha = (alphaKnow.value * 255).toInt()
                                    )
                                )
                                .fillMaxWidth()
                                .graphicsLayer {
                                    rotationY = if (rotation.value >= 90f) 180f else 0f
                                    alpha = alphaKnow.value
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(modifier =Modifier.padding(5.dp), text = "Know", color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(
                                        red = 255,
                                        green = 220,
                                        blue = 40,
                                        alpha = (alphaDontKnow.value * 255).toInt()
                                    )
                                )
                                .fillMaxWidth()
                                .graphicsLayer {
                                    rotationY = if (rotation.value >= 90f) 180f else 0f
                                    alpha = alphaDontKnow.value
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(modifier =Modifier.padding(5.dp), text = "Don't know")
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                rotationY = if (rotation.value >= 90f) 180f else 0f
                            },
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = if (rotation.value >= 90f) word.definition else word.term,
                            fontSize = 26.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}