package com.dictionary.presentation.category_list

import com.dictionary.domain.entity.Category

data class CategoriesListState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String = ""
)