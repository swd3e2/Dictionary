package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.models.WordWithIndex
import com.dictionary.presentation.models.WordsWithSuggest
import java.util.*

class TestState {
    var currentWord = mutableStateOf<WordsWithSuggest?>(null)
    var words = mutableStateListOf<WordsWithSuggest>()
    var wordsState = mutableStateMapOf<Int, String>()
    var index: Int = 0
    private var indexInc = 0
    private var lastAddedWordId = 0

    fun addAll(allWords: MutableList<Word>) {
        if (allWords.size >= 5) {
            val excludedWords = mutableSetOf<Int>()

            for (word in allWords) {
                excludedWords.add(word.id)

                val suggestedWords = mutableListOf(WordWithIndex(word, indexInc++))
                while (suggestedWords.size < 5) {
                    val randomWord = allWords[
                            (0 until allWords.size).filter { !excludedWords.contains(allWords[it].id) }
                                .random()
                    ]
                    if (excludedWords.contains(word.id)) {
                        excludedWords.add(randomWord.id)
                        suggestedWords.add(WordWithIndex(word = randomWord, index = indexInc++))
                    }
                }
                suggestedWords.shuffle()
                words.add(WordsWithSuggest(word = word, suggestedWords.toMutableStateList()))
                excludedWords.clear()
            }
        } else {
            for (word in allWords) {
                val suggestedWords = mutableListOf(WordWithIndex(word = word, index = indexInc++))
                for (w in allWords) if (w.id != word.id) suggestedWords.add(
                    WordWithIndex(
                        word = w,
                        index = indexInc++
                    )
                )
                suggestedWords.shuffle()
                words.add(WordsWithSuggest(word = word, suggestedWords.toMutableStateList()))
            }
        }
    }

    fun addWord(newWord: Word, allWords: MutableList<Word>) {
        if (newWord.id == lastAddedWordId) {
            return
        }
        lastAddedWordId = newWord.id

        if (allWords.size >= 5) {
            val excludedWords = mutableSetOf<Int>()
            excludedWords.add(newWord.id)

            val suggestedWords = mutableListOf(WordWithIndex(word = newWord, index = indexInc++))
            while (suggestedWords.size < 5) {
                val randomWord = allWords[
                        (0 until allWords.size).filter { !excludedWords.contains(allWords[it].id) }
                            .random()
                ]
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
            for (w in allWords) if (w.id != newWord.id) suggestedWords.add(
                WordWithIndex(
                    w,
                    indexInc++
                )
            )
            suggestedWords.shuffle()
            words.add(WordsWithSuggest(word = newWord, suggestedWords.toMutableStateList()))
        }
    }
}
