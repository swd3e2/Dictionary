package com.dictionary.presentation.learn_words.models

import com.dictionary.domain.entity.Word

data class WordWithIndex(
    val word: Word,
    val index: Int
)