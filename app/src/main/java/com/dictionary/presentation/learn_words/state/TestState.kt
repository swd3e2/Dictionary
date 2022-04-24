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

    private var wordCurrentIndex: Int = 0
    private var indexInc = 0
    private var lastAddedWordId = 0
    private lateinit var currentWords: List<Word>

    fun init(allWords: List<Word>) {
        wordsState.clear()
        words.clear()
        currentWords = allWords
        allWords.forEach { addWord(it) }
        currentWord.value = words[wordCurrentIndex]
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
                val randomWord = currentWords[(currentWords.indices).filter { !excludedWords.contains(currentWords[it].id) }.random()]
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
            currentWords.forEach { if (it.id != newWord.id) suggestedWords.add(WordWithIndex(it, indexInc++)) }
            suggestedWords.shuffle()
            words.add(WordsWithSuggest(word = newWord, suggestedWords.toMutableStateList()))
        }
    }

    fun onSelect(word: WordWithIndex): State {
        if (currentWord.value!!.word.id == word.word.id) {
            if (wordCurrentIndex + 1 >= words.size) {
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
        if (wordCurrentIndex + 1 >= words.size) {
            return
        }

        wordCurrentIndex++
        lastAddedWordId = 0
        currentWord.value = words[wordCurrentIndex]
    }

    sealed class State {
        object SelectRight: State()
        object SelectWrong: State()
        object GameEnd: State()
    }
}
