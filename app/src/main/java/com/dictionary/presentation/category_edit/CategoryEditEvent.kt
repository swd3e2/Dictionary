package com.dictionary.presentation.category_edit

import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.presentation.category_list.CategoryListEvent

sealed class CategoryEditEvent {
    object OnMenuClick: CategoryEditEvent()
    object OnCloseMenu: CategoryEditEvent()
    data class OnTermChange(val term: String): CategoryEditEvent()
    data class OnDefinitionChange(val definition: String): CategoryEditEvent()
    object OnWordSaveClick: CategoryEditEvent()
    object OnDeleteWord : CategoryEditEvent()
    data class OnWordClick(val id: Int) : CategoryEditEvent()
    data class OnSearchTermChange(val term: String) : CategoryEditEvent()
    data class OnGameClick(val id: Int) : CategoryEditEvent()
    data class OnLearnClick(val id: Int) : CategoryEditEvent()
    data class OnShowDeleteDialog(val id: Int): CategoryEditEvent()
    object OnHideDeleteDialog: CategoryEditEvent()
    object OnOpenAddWordDialog: CategoryEditEvent()
    object OnCloseAddWordDialog: CategoryEditEvent()
    object GetTranslation: CategoryEditEvent()
}