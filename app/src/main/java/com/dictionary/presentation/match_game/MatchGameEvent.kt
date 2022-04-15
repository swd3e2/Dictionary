package com.dictionary.presentation.match_game

import com.dictionary.presentation.models.WordWithIndex

sealed class MatchGameEvent {
    data class OnMatchSelect(val word: WordWithIndex): MatchGameEvent()
}