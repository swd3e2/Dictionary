package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.models.WordWithIndex
import com.dictionary.presentation.models.WordsWithSuggest

class TestState {
    var currentWord = mutableStateOf<WordsWithSuggest?>(null)
    var words = mutableStateListOf<WordsWithSuggest>()
    var wordsState = mutableStateMapOf<Int, String>()

    private var currentWordIndex: Int = 0
    private var indexInc = 0
    private var lastAddedWordId = 0
    private var currentWords = mutableStateListOf<Word>()

    fun init(allWords: List<Word>) {
        clear()
        currentWords.addAll(allWords)
        allWords.forEach { addWord(it) }
        currentWord.value = words[currentWordIndex]
    }

    private fun addWord(newWord: Word) {
        if (newWord.id == lastAddedWordId) {
            return
        }

        if (currentWords.size >= 5) {
            val excludedWords = mutableSetOf<Int>()
            excludedWords.add(newWord.id)

            val suggestedWords = mutableListOf(WordWithIndex(word = newWord, index = indexInc++))
            while (suggestedWords.size < 5) {
                val randomWord = currentWords[(currentWords.indices).filter {
                    !excludedWords.contains(currentWords[it].id)
                }.random()]
                if (excludedWords.contains(newWord.id)) {
                    excludedWords.add(randomWord.id)
                    suggestedWords.add(WordWithIndex(randomWord, indexInc++))
                }
            }
            suggestedWords.shuffle()
            words.add(WordsWithSuggest(word = newWord, suggestedWords.toMutableStateList()))
            excludedWords.clear()
        } else {
            val suggestedWords = mutableListOf(WordWithIndex(newWord, indexInc++))
            currentWords.forEach {
                if (it.id != newWord.id) suggestedWords.add(
                    WordWithIndex(
                        it,
                        indexInc++
                    )
                )
            }
            suggestedWords.shuffle()
            words.add(WordsWithSuggest(word = newWord, suggestedWords.toMutableStateList()))
        }
    }

    fun onSelect(word: WordWithIndex): State {
        if (currentWord.value!!.word.id == word.word.id) {
            if (canEnd()) {
                return State.GameEnd
            }
            return State.SelectRight
        }

        if (lastAddedWordId != word.word.id) {
            lastAddedWordId = word.word.id
            addWord(currentWord.value!!.word)
        }

        return State.SelectWrong
    }

    fun canEnd(): Boolean {
        return currentWordIndex + 1 >= words.size
    }

    fun setSuccessState(vararg index: Int) {
        index.forEach { wordsState[it] = "success" }
    }

    fun setDeselectedState(vararg index: Int) {
        index.forEach { wordsState[it] = "deselected" }
    }

    fun setErrorState(vararg index: Int) {
        index.forEach { wordsState[it] = "error" }
    }

    fun selectNext() {
        if (currentWordIndex + 1 >= words.size) {
            return
        }

        currentWordIndex++
        lastAddedWordId = 0
        currentWord.value = words[currentWordIndex]
    }

    fun reinitializeFromSavedState(savedState: TestSaveSate, wordsMap: HashMap<Int, Word>) {
        currentWordIndex = savedState.currentIndex
        lastAddedWordId = savedState.lastAddedWordId
        savedState.groups.forEach {
            if (wordsMap.containsKey(it.first)) {
                val suggest = mutableStateListOf<WordWithIndex>()
                it.second.forEach{ key ->
                    if (wordsMap.containsKey(key)) {
                        suggest.add(WordWithIndex(
                            word = wordsMap[key]!!,
                            index = indexInc++
                        ))
                    }
                }

                words.add(
                    WordsWithSuggest(
                        word = wordsMap[it.first]!!,
                        suggested = suggest
                    )
                )
            }
        }
        wordsMap.forEach { i, word -> currentWords.add(word) }
        if (!canEnd()) {
            currentWord.value = words[currentWordIndex]
        }
    }

    fun toSaveState(): TestSaveSate {
        val testSaveState = TestSaveSate()
        testSaveState.currentIndex = currentWordIndex
        testSaveState.lastAddedWordId = lastAddedWordId
        words.forEach {
            testSaveState.groups.add(Pair(it.word.id, it.suggested.map { word -> word.word.id }))
        }
        return testSaveState
    }

    fun clear() {
        currentWordIndex = 0
        indexInc = 0
        lastAddedWordId = 0
        words.clear()
        wordsState.clear()
        currentWords.clear()
        currentWord.value = null
    }

    fun getProgress(size: Int): Float {
        if (currentWordIndex == 0) {
            return 0f
        }
        return currentWordIndex.toFloat() / size
    }

    sealed class State {
        object SelectRight : State()
        object SelectWrong : State()
        object GameEnd : State()
    }
}
