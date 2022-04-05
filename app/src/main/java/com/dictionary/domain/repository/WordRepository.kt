package com.dictionary.domain.repository

import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun categoryWords(category: Int): Flow<List<Word>>
    fun categoryWordsAsList(category: Int): List<Word>
    fun get(id: Int): Word?
    fun delete(id: Int)
    fun deleteByCategory(category: Int)
    fun create(word: Word): Long
    fun update(word: Word)
}