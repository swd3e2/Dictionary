package com.dictionary.presentation.models

data class MatchWordGroup(
    var termWords: MutableList<WordWithIndex> = mutableListOf(),
    var defWords: MutableList<WordWithIndex> = mutableListOf()
)