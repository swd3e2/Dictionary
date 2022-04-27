package com.dictionary.presentation.learn_words.state


data class LearnWordsSavedState(
    val currentStep: Int,
    val currentWords: List<Int>,
    val lastWordWrong: Boolean,
    val matchSaveState: MatchSaveState,
    val testSaveState: TestSaveSate,
    val cardsSaveState: CardsSaveState,
    val writeSaveState: WriteSaveState
)