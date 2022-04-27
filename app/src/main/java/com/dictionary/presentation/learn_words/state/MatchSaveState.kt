package com.dictionary.presentation.learn_words.state

data class MatchSaveState(
    var groups: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf(),
    var currentGroupIndex: Int = 0,
    var removedWords: MutableSet<Int> = mutableSetOf(),
    var successCount: Int = 0
)