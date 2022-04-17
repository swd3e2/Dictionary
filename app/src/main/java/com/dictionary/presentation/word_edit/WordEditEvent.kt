package com.dictionary.presentation.word_edit

sealed class WordEditEvent {
    data class OnTermChange(val term: String): WordEditEvent()
    data class OnDefinitionChange(val definition: String): WordEditEvent()
    data class OnSynonymsChange(val synonyms: String): WordEditEvent()
    data class OnAntonymsChange(val antonyms: String): WordEditEvent()
    data class OnSimilarChange(val similar: String): WordEditEvent()
    data class OnTranscriptionChange(val transcription: String): WordEditEvent()
    data class OnClickOnTranslation(val translation: String) : WordEditEvent()
    object OnSaveClick: WordEditEvent()
    object OnMenuClick: WordEditEvent()
    object OnCloseMenu: WordEditEvent()
    object OnShowTranslationDialog: WordEditEvent()
    object OnHideTranslationDialog: WordEditEvent()
    object OnApplyTranslation: WordEditEvent()
}