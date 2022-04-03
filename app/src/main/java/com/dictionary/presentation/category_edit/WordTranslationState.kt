package com.dictionary.presentation.category_edit

import com.dictionary.domain.entity.Translation


data class WordTranslationState(
    val translation: Translation?,
    val isLoading: Boolean = false
)