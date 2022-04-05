package com.dictionary.presentation.category_list

sealed class CategoryListEvent {
    data class OnCategoryClick(val id: Int): CategoryListEvent()
    object OnSaveClick: CategoryListEvent()
    data class OnChangeTitle(val title: String): CategoryListEvent()
    data class OnDeleteCategory(val id: Int): CategoryListEvent()
    data class OnGameClick(val id: Int): CategoryListEvent()
    object OnOpenAddCategoryDialog: CategoryListEvent()
    object OnCloseAddCategoryDialog: CategoryListEvent()
}