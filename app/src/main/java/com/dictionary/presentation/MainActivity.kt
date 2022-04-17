package com.dictionary.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dictionary.presentation.cards_game.CardsGameScreen
import com.dictionary.presentation.category_edit.CategoryEditScreen
import com.dictionary.presentation.category_list.CategoryListScreen
import com.dictionary.presentation.common.GetFile
import com.dictionary.presentation.common.GetImage
import com.dictionary.presentation.learn_words.LearnWordsScreen
import com.dictionary.presentation.word_edit.WordEditScreen
import com.dictionary.utils.Routes
import dagger.hilt.android.AndroidEntryPoint


//AppCompatActivity
//ComponentActivity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val getFileObserver = GetFile(activityResultRegistry)
        lifecycle.addObserver(getFileObserver)

        val getImageObserver = GetImage(activityResultRegistry)
        lifecycle.addObserver(getImageObserver)

        setContent {
            MaterialTheme(
                colors = Colors(
                    primary = Color(0xff617BF4),
                    primaryVariant = Color(0xff3700b3),
                    secondary = Color(0xff617BF4),
                    secondaryVariant = Color(0xff3700b3),
                    background = Color(0xffF5F8FE),
                    surface = Color(0xffffffff),
                    error = Color(0xffb00020),
                    onPrimary = Color(0xffffffff),
                    onSecondary = Color(0x00000000),
                    onBackground = Color(0x00000000),
                    onSurface = Color(0x00000000),
                    onError = Color(0xffffffff),
                    isLight = true
                )
            ) {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.CATEGORY_LIST
                    ) {
                        composable(
                            route = Routes.CATEGORY_LIST
                        ) {
                            CategoryListScreen(
                                getFile = getFileObserver,
                                onNavigate = { navController.navigate(it.route) },
                            )
                        }
                        composable(
                            route = Routes.CATEGORY_EDIT + "?id={id}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            CategoryEditScreen(
                                getImage = getImageObserver,
                                onNavigate = { navController.navigate(it.route) },
                                onPopBackStack = navController::popBackStack
                            )
                        }
                        composable(
                            route = Routes.WORD_EDIT + "?id={id}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            WordEditScreen(onPopBackStack = {
                                navController.popBackStack()
                            })
                        }
                        composable(
                            route = Routes.CARDS_GAME + "?id={id}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            CardsGameScreen(onPopBackStack = {
                                navController.popBackStack()
                            })
                        }
                        composable(
                            route = Routes.LEARN_WORDS + "?id={id}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            LearnWordsScreen(onPopBackStack = {
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}