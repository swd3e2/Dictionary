package com.dictionary.presentation.word_edit_info

sealed class WordEditEvent {
    object OnMenuClick: WordEditEvent()
    object OnCloseMenu: WordEditEvent()
}