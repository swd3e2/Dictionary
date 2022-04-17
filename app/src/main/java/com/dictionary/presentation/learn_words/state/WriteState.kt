package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.learn_words.LearnWordsEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteState {
    var currentWord = mutableStateOf<Word?>(null)
        private set

    var words = mutableListOf<Word>()
        private set

    var definition = mutableStateOf("")

    var hasError = mutableStateOf(false)
        private set

    var hasDefinition = mutableStateOf("")
        private set
    var wantDefinition = mutableStateOf("")
        private set

    private var currentIndex = 0
    private var lastAddedWordId = 0

    fun init(currentWords: List<Word>) {
        words.addAll(currentWords)
        currentWord.value = words[currentIndex]
    }

    fun tryGuess(): Boolean {
        val word = currentWord.value!!
        val guessedRight = guessedRight(word, definition.value)

        if (guessedRight) {
            definition.value = ""
            hasError.value = false
            return true
        }

        hasError.value = true
        if (lastAddedWordId != word.id) {
            lastAddedWordId = word.id
            words.add(words[currentIndex])
        }

        hasDefinition.value = definition.value
        wantDefinition.value = word.definition
        definition.value = ""
        return false
    }

    fun selectNext() : Boolean {
        if (currentIndex + 1 >= words.size) {
            return false
        }
        hasError.value = false
        lastAddedWordId = 0
        currentIndex++
        currentWord.value = words[currentIndex]
        definition.value = ""
        return true
    }

    private fun guessedRight(word: Word, definition: String): Boolean {
        val possibleDefinitions = word.definition.split(',')

        var guessedRight = false
        for (possibleDefinition in possibleDefinitions) {
            guessedRight = guessedRight || possibleDefinition.trim()
                .lowercase() == definition.lowercase()
        }
        return guessedRight
    }
}