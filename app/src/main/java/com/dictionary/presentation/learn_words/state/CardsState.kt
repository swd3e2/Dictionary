package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateOf
import com.dictionary.domain.entity.Word

class CardsState {
    var currentWord = mutableStateOf<Word?>(null)
    var words = mutableListOf<Word>()
    var index = 0

    fun addAll(allWords: MutableList<Word>) {
        words.addAll(allWords)
    }
}