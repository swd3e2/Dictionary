package com.dictionary.presentation.category_list

import android.net.Uri
import com.dictionary.domain.entity.CategoryWithWords

sealed class CategoryListEvent {
    data class OnCategoryClick(val id: Int): CategoryListEvent()
    object OnSaveClick: CategoryListEvent()
    data class OnChangeTitle(val title: String): CategoryListEvent()
    object OnDeleteCategory: CategoryListEvent()
    data class OnShowDeleteDialog(val category: CategoryWithWords): CategoryListEvent()
    object OnHideDeleteDialog: CategoryListEvent()
    object OnOpenAddCategoryDialog: CategoryListEvent()
    object OnCloseAddCategoryDialog: CategoryListEvent()
    data class OnImportFile(val uri: Uri): CategoryListEvent()
}