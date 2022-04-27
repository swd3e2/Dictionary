package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateOf
import com.dictionary.domain.entity.Word

class CardsState {
    var currentWord = mutableStateOf<Word?>(null)
        private set

    private var words = mutableListOf<Word>()
    private var currentWordIndex: Int = 0

    fun init(allWords: MutableList<Word>) {
        clear()
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

    fun canEnd(): Boolean {
        return currentWordIndex + 1 >= words.size
    }

    fun reinitializeFromSavedState(savedState: CardsSaveState, wordsMap: HashMap<Int, Word>) {
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

    fun toSaveState(): CardsSaveState {
        val saveState = CardsSaveState()
        saveState.currentIndex = currentWordIndex
        words.forEach { saveState.words.add(it.id) }
        return saveState
    }

    fun clear() {
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