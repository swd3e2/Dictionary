package com.dictionary.presentation.components

import android.graphics.drawable.Icon
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(
    navController: NavHostController
) {
    BottomNavigation {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        BottomNavigationItem(
            selected = currentRoute == "asd",
            onClick = {
                navController.navigate("asd") {
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
                Text(text = "Test")
            },
        )
        BottomNavigationItem(
            selected = currentRoute == "asd",
            onClick = {
                navController.navigate("asd") {
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
                Text(text = "Test")
            },
        )
        BottomNavigationItem(
            selected = currentRoute == "asd",
            onClick = {
                navController.navigate("asd") {
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
                Text(text = "Test")
            },
        )
    }
}