package com.dictionary.presentation.cards_game

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dictionary.presentation.cards_game.components.DropDownMenu
import com.dictionary.presentation.cards_game.components.WordCard
import com.dictionary.presentation.common.DisabledInteractionSource
import com.dictionary.presentation.utils.FlipCard
import com.dictionary.ui.theme.PrimaryTextColor
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardsGameList(
    onPopBackStack: () -> Unit = {},
    viewModel: CardsGameViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            DropDownMenu(onPopBackStack)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "3 / 10",
                letterSpacing = 3.sp,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryTextColor
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.currentWord.value != null) {
                WordCard(onEvent = viewModel::onEvent, word = viewModel.currentWord.value!!)
            } else {
                Text(text = "No words to learn")
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun FlipRotate(
    flipCard: FlipCard,
    onClick: (FlipCard) -> Unit,
    modifier: Modifier = Modifier,
    previous: @Composable () -> Unit = {},
    forward: @Composable () -> Unit = {}
) {
//
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(55.dp)
//                .background(MaterialTheme.colors.primary),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//        ) {
//            Text(
//                text = "Flip Rotation",
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Card(
//                onClick = { onClick(flipCard) },
//                modifier = modifier
//                    .fillMaxSize()
//                    .graphicsLayer {
//                        rotationY = rotation.value
//                        cameraDistance = 12f * density
//                    },
//                elevation = 10.dp,
//                shape = RoundedCornerShape(10.dp)
//            ) {
//                if (rotation.value <= 90f) {
//                    Box(
//                        Modifier.fillMaxSize()
//                    ) {
//                        forward()
//                    }
//                } else {
//                    Box(
//                        Modifier
//                            .fillMaxSize()
//                            .graphicsLayer {
//                                rotationY = 180f
//                            }
//                    ) {
//                        previous()
//                    }
//                }
//            }
//        }
//    }
}