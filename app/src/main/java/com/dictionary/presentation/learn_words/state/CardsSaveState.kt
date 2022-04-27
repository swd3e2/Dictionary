package com.dictionary.presentation.learn_words.state

data class CardsSaveState(
    val words: MutableList<Int> = mutableListOf(),
    var currentIndex: Int = 0
)