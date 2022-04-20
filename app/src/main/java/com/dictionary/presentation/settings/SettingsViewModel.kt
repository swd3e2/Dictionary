package com.dictionary.presentation.settings

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {
    var darkTheme = mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val prefs = getApplication<Application>().getSharedPreferences("mysettings", Context.MODE_PRIVATE)
        darkTheme.value = prefs.getBoolean("dark_theme", false)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnChangeDarkTheme -> {
                val prefs = getApplication<Application>().getSharedPreferences("mysettings", Context.MODE_PRIVATE)
                prefs.edit().apply{
                    putBoolean("dark_theme", !darkTheme.value)
                }.apply()
                darkTheme.value = !darkTheme.value
            }
        }
    }
}