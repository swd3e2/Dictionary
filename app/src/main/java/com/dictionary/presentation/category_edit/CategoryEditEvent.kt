package com.dictionary.presentation.category_edit

import android.net.Uri

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
    data class OnMatchGameClick(val id: Int) : CategoryEditEvent()
    data class OnLearnClick(val id: Int) : CategoryEditEvent()
    data class OnShowDeleteDialog(val id: Int): CategoryEditEvent()
    data class OnTitleChange(var title: String) : CategoryEditEvent()
    data class OnSortChange(var field: String, var direction: String) : CategoryEditEvent()
    object OnTitleSave : CategoryEditEvent()
    object OnHideDeleteDialog: CategoryEditEvent()
    object OnShowAddWordDialog: CategoryEditEvent()
    object OnCloseAddWordDialog: CategoryEditEvent()
    object GetTranslation: CategoryEditEvent()
    object OnShowSortDialog: CategoryEditEvent()
    object OnHideSortDialog: CategoryEditEvent()
    data class OnImagePickFile(val uri: Uri): CategoryEditEvent()
}