package com.dictionary.presentation.category_list

import android.net.Uri
import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.presentation.category_edit.CategoryEditEvent

sealed class CategoryListEvent {
    data class OnCategoryClick(val id: Int): CategoryListEvent()
    object OnSaveClick: CategoryListEvent()
    data class OnChangeTitle(val title: String): CategoryListEvent()
    object OnDeleteCategory: CategoryListEvent()
    data class OnShowDeleteDialog(val category: CategoryWithWords): CategoryListEvent()
    object OnHideDeleteDialog: CategoryListEvent()
    object OnShowAddCategoryDialog: CategoryListEvent()
    object OnHideAddCategoryDialog: CategoryListEvent()
    object OnGoToCardsGame: CategoryListEvent()
    object OnGoToMatchGame: CategoryListEvent()
    object OnGoToLearnWords: CategoryListEvent()
    data class OnImportFile(val uri: Uri): CategoryListEvent()
    data class OnSearchChange(val search: String) : CategoryListEvent()
}