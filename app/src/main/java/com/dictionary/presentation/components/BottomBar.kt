package com.dictionary.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dictionary.utils.Routes

@Composable
fun BottomBar(
    navController: NavHostController,
    fullWidth: Boolean = false
) {
    BottomAppBar(
        modifier = Modifier
            .height(55.dp),
        cutoutShape = MaterialTheme.shapes.small.copy(
            CornerSize(percent = 40)
        ),
        content = {
            BottomNavigation {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                BottomNavigationItem(
                    selected = currentRoute == Routes.CATEGORY_LIST,
                    onClick = {
                        navController.navigate(Routes.CATEGORY_LIST) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(imageVector = Icons.Default.List, contentDescription = "List")
                    },
                    label = {
                        Text(text = "List")
                    },
                )
                BottomNavigationItem(
                    selected = currentRoute == Routes.SEARCH_WORDS,
                    onClick = {
                        navController.navigate(Routes.SEARCH_WORDS) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    label = {
                        Text(text = "Search")
                    },
                )
                BottomNavigationItem(
                    selected = currentRoute == Routes.SETTINGS,
                    onClick = {
                        navController.navigate(Routes.SETTINGS) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    },
                    label = {
                        Text(text = "Settings")
                    },
                )
                if (!fullWidth) {
                    Spacer(modifier = Modifier.weight(1f, true))
                }
            }
        }
    )
}