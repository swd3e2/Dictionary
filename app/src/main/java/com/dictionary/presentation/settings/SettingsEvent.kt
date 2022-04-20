package com.dictionary.presentation.settings

sealed class SettingsEvent {
    object OnChangeDarkTheme: SettingsEvent()
}