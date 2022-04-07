package com.dictionary.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithWords(
    @Embedded val category: Category,
    @Relation(parentColumn = "id", entityColumn = "category")
    val words: List<Word>
){
    fun countWordsToLearn(): Int {
        var count = 0
        for (word in words) {
            if (word.shouldBeLearned()) {
                count++
            }
        }
        return count
    }
}