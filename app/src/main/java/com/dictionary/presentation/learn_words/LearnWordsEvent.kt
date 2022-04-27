package com.dictionary.presentation.learn_words

import com.dictionary.domain.entity.Word
import com.dictionary.presentation.models.WordWithIndex

sealed class LearnWordsEvent {
    object OnGoToMatch: LearnWordsEvent()
    object OnGoToTest: LearnWordsEvent()
    object OnGoToCards: LearnWordsEvent()
    object OnGoToWrite: LearnWordsEvent()
    object OnGoToDone: LearnWordsEvent()
    data class OnMatchSelect(val word: WordWithIndex): LearnWordsEvent()
    data class OnTestSelect(val word: Word, val selected: WordWithIndex): LearnWordsEvent()
    data class OnCardLeftSwipe(val word: Word): LearnWordsEvent()
    data class OnCardRightSwipe(val word: Word): LearnWordsEvent()
    object OnStartNew: LearnWordsEvent()
    data class OnWriteTryDefinition(val definition: String): LearnWordsEvent()
    data class OnBack(val end: Boolean): LearnWordsEvent()
    object OnContinue: LearnWordsEvent()
    object OnRestart: LearnWordsEvent()
}