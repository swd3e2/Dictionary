package com.dictionary.presentation.cards_game

import com.dictionary.domain.entity.Word

sealed class CardsGameEvent {
    data class WordLearned(val word: Word): CardsGameEvent()
    data class WordNotLearned(val word: Word): CardsGameEvent()
}