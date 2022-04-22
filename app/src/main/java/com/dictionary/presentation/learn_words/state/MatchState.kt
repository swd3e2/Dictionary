package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.models.WordWithIndex

class MatchState {
    var totalCountWordsCount: Int = 0
    var currentWordsGroup: SnapshotStateList<WordWithIndex> = mutableStateListOf()

    var wordsState: SnapshotStateMap<Int, String> = mutableStateMapOf()

    private var wordsSelected: MutableList<WordWithIndex> = mutableListOf()
    private var wordsGroups: MutableList<MutableList<WordWithIndex>> = mutableListOf()
    private var currentWordsGroupIndex: Int = 0
    private var successCount: Int = 0

    fun init(words: List<Word>) {
        currentWordsGroupIndex = 0
        totalCountWordsCount = words.size
        wordsGroups = getGroups(words)
        currentWordsGroup.addAll(wordsGroups[currentWordsGroupIndex])
    }

    private fun getGroups(words: List<Word>): MutableList<MutableList<WordWithIndex>> {
        val groups: MutableList<MutableList<WordWithIndex>> = mutableListOf()
        val listSize = words.size

        var group = mutableListOf<WordWithIndex>()
        for ((index, word) in words.withIndex()) {
            if (group.size == 12) {
                group.shuffle()
                groups.add(group)
                group = mutableListOf()
            }
            group.add(WordWithIndex(word = word, index = index))
            group.add(WordWithIndex(word = word, index = index + listSize))

            wordsState[index] = "unselected"
            wordsState[index + listSize] = "unselected"
        }

        if (group.size > 0) {
            group.shuffle()
            groups.add(group)
        }

        return groups
    }

    fun onWordSelect(word: WordWithIndex): State {
        if (wordsState[word.index] == "selected") {
            wordsSelected.remove(word)
            return State.WordDeselected
        }

        wordsSelected.add(word)

        if (wordsSelected.size == 2) {
            val first = wordsSelected[0]
            val second = wordsSelected[1]
            wordsSelected.clear()

            return if (first.word.id == second.word.id) {
                successCount += 2
                State.MatchRight(first, second)
            } else {
                State.MatchWrong(first, second)
            }
        }

        return State.WordSelected
    }

    fun selectNextGroup(): Boolean {
        currentWordsGroupIndex++
        if (currentWordsGroupIndex >= wordsGroups.size) {
            return false
        }
        currentWordsGroup.clear()
        currentWordsGroup.addAll(wordsGroups[currentWordsGroupIndex])
        return true
    }

    fun resetSuccessCount() {
        successCount = 0
    }

    fun setSuccessState(vararg index: Int) {
        index.forEach { wordsState[it] = "success" }
    }

    fun setHideState(vararg index: Int) {
        index.forEach { wordsState[it] = "hide" }
    }

    fun setSelectedState(vararg index: Int) {
        index.forEach { wordsState[it] = "selected" }
    }

    fun setDeselectedState(vararg index: Int) {
        index.forEach { wordsState[it] = "deselected" }
    }

    fun setErrorState(vararg index: Int) {
        index.forEach { wordsState[it] = "error" }
    }

    fun canGoNextGroup(): Boolean {
        return successCount == currentWordsGroup.size
    }

    sealed class State {
        data class MatchRight(val first: WordWithIndex, val second: WordWithIndex): State()
        data class MatchWrong(val first: WordWithIndex, val second: WordWithIndex): State()
        object WordSelected: State()
        object WordDeselected: State()
    }
}