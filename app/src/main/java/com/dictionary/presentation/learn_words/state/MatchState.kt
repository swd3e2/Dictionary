package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.models.MatchWordGroup
import com.dictionary.presentation.models.WordWithIndex

class MatchState {
    var currentWordsGroup = mutableStateOf(MatchWordGroup())
    var wordsState: SnapshotStateMap<Int, String> = mutableStateMapOf()

    var wordsSelected: MutableList<WordWithIndex> = mutableListOf()
    var wordsGroups: MutableList<MatchWordGroup> = mutableListOf()
    var currentWordsGroupIndex: Int = 0
    var successCount: Int = 0
    var indexInc = 0

    private val removedWords = mutableSetOf<Int>()

    fun init(words: List<Word>) {
        clear()
        wordsGroups = getGroups(words)
        currentWordsGroup.value = wordsGroups[currentWordsGroupIndex]
    }

    private fun getGroups(words: List<Word>): MutableList<MatchWordGroup> {
        val groups: MutableList<MatchWordGroup> = mutableListOf()
        val listSize = words.size

        var group = MatchWordGroup()
        for ((index, word) in words.withIndex()) {
            if (group.termWords.size + group.defWords.size == 12) {
                group.termWords.shuffle()
                group.defWords.shuffle()
                groups.add(group)
                group = MatchWordGroup()
            }
            group.termWords.add(WordWithIndex(word = word, index = indexInc++))
            group.defWords.add(WordWithIndex(word = word, index = indexInc++))

            wordsState[index] = "unselected"
            wordsState[index + listSize] = "unselected"
        }

        if (group.termWords.size > 0) {
            group.termWords.shuffle()
            group.defWords.shuffle()
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
                removedWords.add(first.word.id)
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
        currentWordsGroup.value = wordsGroups[currentWordsGroupIndex]
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
        return successCount == currentWordsGroup.value.termWords.size + currentWordsGroup.value.defWords.size
    }

    fun reinitializeFromSavedState(savedState: MatchSaveState, wordsMap: HashMap<Int, Word>) {
        currentWordsGroupIndex = savedState.currentGroupIndex
        successCount = savedState.successCount

        savedState.groups.forEach {
            val wordGroup = MatchWordGroup()
            it.forEach { pair ->
                val termIndex = indexInc++
                val defIndex = indexInc++

                if (!wordsMap.containsKey(pair.first)) {
                    wordGroup.termWords.add(WordWithIndex(word = Word(), index = termIndex))
                    wordsState[termIndex] = "hide"
                    successCount += 1
                } else {
                    val termWord = wordsMap[pair.first]!!
                    wordGroup.termWords.add(WordWithIndex(word = termWord, index = termIndex))
                    if (savedState.removedWords.contains(termWord.id)) {
                        wordsState[termIndex] = "hide"
                    }
                }

                if (!wordsMap.containsKey(pair.second)) {
                    wordGroup.defWords.add(WordWithIndex(word = Word(), index = defIndex))
                    wordsState[defIndex] = "hide"
                    successCount += 1
                } else {
                    val defWord = wordsMap[pair.second]!!
                    wordGroup.defWords.add(WordWithIndex(word = defWord, index = defIndex))
                    if (savedState.removedWords.contains(defWord.id)) {
                        wordsState[defIndex] = "hide"
                    }
                }
            }
            wordsGroups.add(wordGroup)
        }
        savedState.removedWords.forEach {
            if (wordsMap.containsKey(it)) {
                removedWords.add(it)
            }
        }
        currentWordsGroup.value = wordsGroups[currentWordsGroupIndex]
    }

    fun toSaveState(): MatchSaveState {
        val saveState = MatchSaveState()

        wordsGroups.forEach { matchStateGroup ->
            val currentGroup = mutableListOf<Pair<Int, Int>>()
            matchStateGroup.termWords.forEachIndexed { index, _ ->
                currentGroup.add(
                    Pair(
                        matchStateGroup.termWords[index].word.id,
                        matchStateGroup.defWords[index].word.id
                    )
                )
            }
            saveState.groups.add(currentGroup)
        }
        saveState.removedWords = removedWords
        saveState.successCount = successCount
        saveState.currentGroupIndex = currentWordsGroupIndex

        return saveState
    }

    fun clear() {
        wordsState.clear()
        wordsGroups.clear()
        removedWords.clear()
        currentWordsGroupIndex = 0
        successCount = 0
        indexInc = 0
        currentWordsGroup.value = MatchWordGroup()
    }

    fun getProgress(count: Int): Float {
        return removedWords.size.toFloat() / count
    }

    sealed class State {
        data class MatchRight(val first: WordWithIndex, val second: WordWithIndex) : State()
        data class MatchWrong(val first: WordWithIndex, val second: WordWithIndex) : State()
        object WordSelected : State()
        object WordDeselected : State()
    }
}