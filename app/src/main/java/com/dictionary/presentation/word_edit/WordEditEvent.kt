package com.dictionary.presentation.word_edit

sealed class WordEditEvent {
    object OnMenuClick: WordEditEvent()
    object OnCloseMenu: WordEditEvent()
}