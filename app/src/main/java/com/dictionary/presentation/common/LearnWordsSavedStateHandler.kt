package com.dictionary.presentation.common

import androidx.lifecycle.SavedStateHandle
import com.dictionary.presentation.learn_words.state.LearnWordsSavedState
import com.google.gson.Gson

class LearnWordsSavedStateHandler {
    val savedStateHandler: SavedStateHandle = SavedStateHandle()

    fun getSavedState(id: Int): LearnWordsSavedState? {
        val savedState = savedStateHandler.get<String>("saved$id") ?: return null
        return Gson().fromJson(savedState, LearnWordsSavedState::class.java)
    }

    fun hasSavedState(id: Int): Boolean {
        return savedStateHandler.get<String>("saved$id") != null
    }

    fun clearSavedState(id: Int) {
        savedStateHandler.remove<String>("saved$id")
    }
}