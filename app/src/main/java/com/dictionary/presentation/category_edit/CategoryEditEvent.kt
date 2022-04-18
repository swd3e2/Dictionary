package com.dictionary.presentation.category_edit

import android.net.Uri
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.category_list.CategoryListEvent

sealed class CategoryEditEvent {
    object OnMenuClick: CategoryEditEvent()
    object OnCloseMenu: CategoryEditEvent()

    data class OnWordClick(val id: Int) : CategoryEditEvent()
    data class OnSearchTermChange(val term: String) : CategoryEditEvent()
    data class OnGameClick(val id: Int) : CategoryEditEvent()
    data class OnLearnClick(val id: Int) : CategoryEditEvent()

    data class OnShowWordDeleteDialog(val word: Word): CategoryEditEvent()
    object OnHideWordDeleteDialog: CategoryEditEvent()
    object OnDeleteWord: CategoryEditEvent()

    data class OnDropWordBucket(val word: Word): CategoryEditEvent()
    data class OnTitleChange(var title: String) : CategoryEditEvent()
    data class OnSortChange(var field: String, var direction: String) : CategoryEditEvent()
    object OnTitleSave : CategoryEditEvent()

    object OnShowSortDialog: CategoryEditEvent()
    object OnHideSortDialog: CategoryEditEvent()

    data class OnImagePickFile(val uri: Uri): CategoryEditEvent()

    data class OnShowMoveToCategoryDialog(val word: Word): CategoryEditEvent()
    object OnHideMoveToCategoryDialog: CategoryEditEvent()

    object OnAddWord : CategoryEditEvent()

    data class OnMoveToCategoryDialog(val category: Category): CategoryEditEvent()

    object OnDeleteCategory: CategoryEditEvent()
    object OnShowDeleteCategoryDialog: CategoryEditEvent()
    object OnHideDeleteCategoryDialog: CategoryEditEvent()
}