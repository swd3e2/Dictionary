package com.dictionary.presentation.common

import android.app.Application
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow

class Theme(
    application: Application
) {
    var theme = MutableStateFlow(false)

    init{
        val prefs = application.getSharedPreferences("mysettings", Context.MODE_PRIVATE)
        theme.value = prefs.getBoolean("dark_theme", false)
    }

    fun switch() {
        theme.value = !theme.value
    }
}