package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateOf
import com.dictionary.domain.entity.Word

class CardsState {
    var currentWord = mutableStateOf<Word?>(null)
        private set

    private var words = mutableListOf<Word>()
    private var currentWordIndex = 0

    fun init(allWords: MutableList<Word>) {
        words.addAll(allWords)
        currentWord.value = words[currentWordIndex]
    }

    fun selectNext() {
        currentWordIndex++
        currentWord.value = words[currentWordIndex]
    }

    fun doesNotKnowWord() {
        words.add(words[currentWordIndex])
    }

    fun noMoreWords(): Boolean {
        return currentWordIndex + 1 >= words.size
    }
}