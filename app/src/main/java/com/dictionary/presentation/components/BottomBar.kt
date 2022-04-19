package com.dictionary.presentation.components

import android.graphics.drawable.Icon
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.utils.Routes

@Composable
fun BottomBar(
    navController: NavHostController
) {
    BottomNavigation(
        backgroundColor = Color(0x00000000),
        elevation = 0.dp
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BottomNavigationItem(
            selected = currentRoute == Routes.CATEGORY_LIST,
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = PrimaryTextColor,
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
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = PrimaryTextColor,
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
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = PrimaryTextColor,
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
    }
}