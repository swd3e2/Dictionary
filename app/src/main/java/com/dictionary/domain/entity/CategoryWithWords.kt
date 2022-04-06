package com.dictionary.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithWords(
    @Embedded val category: Category,
    @Relation(parentColumn = "id", entityColumn = "category")
    val words: List<Word>
)