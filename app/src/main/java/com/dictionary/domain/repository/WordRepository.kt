package com.dictionary.domain.repository

import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun categoryWords(category: Int): Flow<List<Word>>
    fun categoryWordsLike(category: Int, term: String): Flow<List<Word>>
    fun list(): Flow<List<Word>>
    fun listLike(term: String): Flow<List<Word>>
    suspend fun categoryWordsAsList(category: Int): List<Word>
    suspend fun asList(): List<Word>
    suspend fun get(id: Int): Word?
    suspend fun delete(id: Int)
    suspend fun deleteByCategory(category: Int)
    suspend fun create(word: Word): Long
    suspend fun batchCreate(words: List<Word>)
    suspend fun update(word: Word)
    suspend fun exists(term: String): Boolean
}