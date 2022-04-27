package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateOf
import com.dictionary.domain.entity.Word

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

    private var currentWordIndex = 0
    private var lastAddedWordId = 0

    fun init(currentWords: List<Word>) {
        clear()
        words.addAll(currentWords)
        currentWord.value = words[currentWordIndex]
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
            words.add(words[currentWordIndex])
        }

        hasDefinition.value = definition.value
        wantDefinition.value = word.definition
        definition.value = ""
        return false
    }

    fun selectNext() : Boolean {
        if (canEnd()) {
            return false
        }
        hasError.value = false
        lastAddedWordId = 0
        currentWordIndex++
        currentWord.value = words[currentWordIndex]
        definition.value = ""
        return true
    }

    fun canEnd(): Boolean {
        return currentWordIndex + 1 >= words.size
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

    fun reinitializeFromSavedState(savedState: WriteSaveState, wordsMap: HashMap<Int, Word>) {
        currentWordIndex = savedState.currentIndex
        savedState.words.forEach {
            if (wordsMap.containsKey(it)) {
                words.add(wordsMap[it]!!)
            }
        }
        if (!canEnd()) {
            currentWord.value = words[currentWordIndex]
        }
    }

    fun toSaveState(): WriteSaveState {
        val saveState = WriteSaveState()
        saveState.currentIndex = currentWordIndex
        words.forEach { saveState.words.add(it.id) }
        return saveState
    }

    fun clear() {
        hasError.value = false
        hasDefinition.value = ""
        wantDefinition.value = ""
        currentWordIndex = 0
        words.clear()
        currentWord.value = null
    }

    fun getProgress(size: Int): Float {
        if (currentWordIndex == 0) {
            return 0f
        }
        return currentWordIndex.toFloat() / size
    }
}