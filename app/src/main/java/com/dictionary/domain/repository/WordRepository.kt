package com.dictionary.domain.repository

import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
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
    suspend fun category(term: String): String?

    fun categoryWords(category: Int): Flow<List<Word>>
    fun categoryWordsSortByTerm(id: Int, asc: Boolean): Flow<List<Word>>
    fun categoryWordsSortByCreated(id: Int, asc: Boolean): Flow<List<Word>>
    fun categoryWordsSortByLastRepeated(id: Int, asc: Boolean): Flow<List<Word>>

    fun categoryWordsLike(category: Int, term: String): Flow<List<Word>>
    fun categoryWordsLikeSortByTerm(id: Int, search: String, asc: Boolean): Flow<List<Word>>
    fun categoryWordsLikeSortByCreated(id: Int, search: String, asc: Boolean): Flow<List<Word>>
    fun categoryWordsLikeSortByLastRepeated(id: Int, search: String, asc: Boolean): Flow<List<Word>>
}