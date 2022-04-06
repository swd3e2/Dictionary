package com.dictionary.domain.repository

import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun categoryWords(category: Int): Flow<List<Word>>
    suspend fun categoryWordsAsList(category: Int): List<Word>
    suspend fun get(id: Int): Word?
    suspend fun delete(id: Int)
    suspend fun deleteByCategory(category: Int)
    suspend fun create(word: Word): Long
    suspend fun update(word: Word)
    suspend fun exists(term: String): Boolean
}