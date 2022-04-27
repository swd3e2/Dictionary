package com.dictionary.presentation.common

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

const val SETTINGS = "mysettings"
const val DARK_THEME = "dark_theme"
const val COUNT_OF_WORDS_TO_LEARN = "count_of_words_to_learn"

class Settings @Inject constructor(
    val application: Application
) {
    var darkTheme = MutableStateFlow(false)
    var countWordsToLearn = mutableStateOf<Int>(0)

    init{
        val prefs = application.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        darkTheme.value = prefs.getBoolean(DARK_THEME, false)
        countWordsToLearn.value = prefs.getInt(COUNT_OF_WORDS_TO_LEARN, 18)
    }

    fun switchDarkTheme() {
        val prefs = application.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        prefs.edit().apply{
            putBoolean(DARK_THEME, !darkTheme.value)
        }.apply()
        darkTheme.value = !darkTheme.value
    }

    fun setCountOfLearnWords(count: Int) {
        val prefs = application.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        prefs.edit().apply{
            putInt(COUNT_OF_WORDS_TO_LEARN, count)
        }.apply()
        countWordsToLearn.value = count
    }
}