package com.dictionary.domain.entity

data class TranslationWord(
    val word: String,
    val type: String,
    val translation: List<String>
)