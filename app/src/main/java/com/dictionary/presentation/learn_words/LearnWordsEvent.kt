package com.dictionary.presentation.learn_words

import com.dictionary.domain.entity.Word
import com.dictionary.presentation.learn_words.models.LearnWord

sealed class LearnWordsEvent {
    object OnGoToMatch: LearnWordsEvent()
    object OnGoToTest: LearnWordsEvent()
    object OnGoToCards: LearnWordsEvent()
    object OnGoToWrite: LearnWordsEvent()
    object OnGoToDone: LearnWordsEvent()
    data class OnMatchSelect(val word: LearnWord): LearnWordsEvent()
    data class OnTestSelect(val word: Word, val wordId: Int, val index: Int): LearnWordsEvent()
    data class OnCardLeftSwipe(val word: Word): LearnWordsEvent()
    data class OnCardRightSwipe(val word: Word): LearnWordsEvent()
    object OnStartNew: LearnWordsEvent()
    data class OnWriteTryDefinition(val definition: String): LearnWordsEvent()
}