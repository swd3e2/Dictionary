package com.dictionary.presentation.models

import com.dictionary.domain.entity.Word

data class WordWithIndex(
    val word: Word,
    val index: Int
)