package com.dictionary.presentation.common

import android.app.Application
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

const val SETTINGS = "mysettings"

class Settings @Inject constructor(
    val application: Application
) {

    var darkTheme = MutableStateFlow(false)

    init{
        val prefs = application.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        darkTheme.value = prefs.getBoolean("dark_theme", false)
    }

    fun switchDarkTheme() {
        val prefs = application.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        prefs.edit().apply{
            putBoolean("dark_theme", !darkTheme.value)
        }.apply()
        darkTheme.value = !darkTheme.value
    }
}