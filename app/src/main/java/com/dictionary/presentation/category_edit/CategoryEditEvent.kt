package com.dictionary.presentation.category_edit

sealed class CategoryEditEvent {
    object OnMenuClick: CategoryEditEvent()
    object OnCloseMenu: CategoryEditEvent()
    data class OnTermChange(val term: String): CategoryEditEvent()
    data class OnDefinitionChange(val definition: String): CategoryEditEvent()
    object OnWordSaveClick: CategoryEditEvent()
    data class OnDeleteWord(val id: Int) : CategoryEditEvent()
    data class OnWordClick(val id: Int) : CategoryEditEvent()
    object OnOpenAddWordDialog: CategoryEditEvent()
    object OnCloseAddWordDialog: CategoryEditEvent()
    object GetTranslation: CategoryEditEvent()
}