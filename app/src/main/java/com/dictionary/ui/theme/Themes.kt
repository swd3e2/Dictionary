package com.dictionary.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val lightTheme = lightColors(
    primary = Color(0xff617BF4),
    primaryVariant = Color(0xff3700b3),
    secondary = Color(0xff617BF4),
    secondaryVariant = Color(0xff3700b3),
    background = Color(0xffF5F8FE),
    surface = Color(0xffffffff),
    error = Color(0xffb00020),
    onBackground = Color(0xFF1E1E1E),
    onSurface = Color(0xFF1E1E1E)   ,
    onError = Color(0xffffffff),
)

val darkTheme = darkColors(
    primary = Color(0xffBB86FC),
    primaryVariant = Color(0xff3700b3),
    secondary = Color(0xFF0376DA),
    secondaryVariant = Color(0xFF025988),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFCF6679),
    onPrimary = Color(0xFF000000),
    onBackground = Color(0xFFE2E2E2),
    onSurface = Color(0xFFE2E2E2),
    onError = Color(0xffffffff),
)