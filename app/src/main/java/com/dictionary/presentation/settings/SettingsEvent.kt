package com.dictionary.presentation.settings

sealed class SettingsEvent {
    object OnChangeDarkTheme: SettingsEvent()
    data class OnChangeCountOfWordsToLearn(val count: Int): SettingsEvent()
}