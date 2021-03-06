package com.dictionary.presentation.settings

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.dictionary.presentation.common.Settings
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val settings: Settings
): ViewModel() {
    var darkTheme = mutableStateOf(false)
        private set

    var countWordsToLearn = mutableStateOf(0)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        darkTheme.value = settings.darkTheme.value
        countWordsToLearn.value = settings.countWordsToLearn.value
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnChangeDarkTheme -> {
                settings.switchDarkTheme()
                darkTheme.value = !darkTheme.value
            }
            is SettingsEvent.OnChangeCountOfWordsToLearn -> {
                settings.setCountOfLearnWords(event.count)
                countWordsToLearn.value = event.count
            }
        }
    }
}