package com.dictionary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dictionary.presentation.cards_game.CardsGameScreen
import com.dictionary.presentation.category_edit.CategoryEditScreen
import com.dictionary.presentation.category_list.CategoryListScreen
import com.dictionary.presentation.common.Settings
import com.dictionary.presentation.common.lifecycle_observer.GetFileLifecycleObserver
import com.dictionary.presentation.common.lifecycle_observer.GetImageLifecycleObserver
import com.dictionary.presentation.learn_words.LearnWordsScreen
import com.dictionary.presentation.search_words.SearchWordsScreen
import com.dictionary.presentation.settings.SettingsScreen
import com.dictionary.presentation.word_edit.WordEditScreen
import com.dictionary.ui.theme.darkTheme
import com.dictionary.ui.theme.lightTheme
import com.dictionary.utils.Routes
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//AppCompatActivity
//ComponentActivity
@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    @Inject lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val getFileObserver = GetFileLifecycleObserver(activityResultRegistry)
        val getImageObserver = GetImageLifecycleObserver(activityResultRegistry)
        lifecycle.addObserver(getFileObserver)
        lifecycle.addObserver(getImageObserver)

        setContent {
            MaterialTheme(
                colors = if (settings.darkTheme.collectAsState(false).value) darkTheme else lightTheme
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
                                navController = navController,
                                getFileLifecycleObserver = getFileObserver,
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
                                getImageLifecycleObserver = getImageObserver,
                                onNavigate = { navController.navigate(it.route) },
                                navController = navController,
                                onPopBackStack = navController::popBackStack
                            )
                        }
                        composable(
                            route = Routes.WORD_EDIT + "?id={id}&category={category}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name = "category") {
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
                        composable(
                            route = Routes.SEARCH_WORDS,
                        ) {
                            SearchWordsScreen(
                                onPopBackStack = { navController.popBackStack() },
                                navController = navController,
                                onNavigate = { navController.navigate(it.route) }
                            )
                        }
                        composable(
                            route = Routes.SETTINGS,
                        ) {
                            SettingsScreen(
                                onPopBackStack = { navController.popBackStack() },
                                navController = navController,
                                onNavigate = { navController.navigate(it.route) }
                            )
                        }
                    }
                }
            }
        }
    }
}