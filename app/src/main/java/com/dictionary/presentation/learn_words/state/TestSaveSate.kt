package com.dictionary.presentation.learn_words.state

data class TestSaveSate(
    val groups: MutableList<Pair<Int, List<Int>>> = mutableListOf(),
    var currentIndex: Int = 0,
    var lastAddedWordId: Int = 0
)