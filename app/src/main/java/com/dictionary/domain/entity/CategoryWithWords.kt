package com.dictionary.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithWords(
    @Embedded val category: Category,
    @Relation(parentColumn = "id", entityColumn = "category")
    val words: List<Word>
){
    fun countWordsToRepeat(): Int {
        return words.filter { it.shouldBeRepeated() }.count()
    }
    fun countWordsToLearn(): Int {
        return words.filter { it.bucket == 0 }.count()
    }
}