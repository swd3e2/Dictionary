package com.dictionary.domain.repository

import com.dictionary.domain.entity.CategoryCount
import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun flowList(): Flow<List<Word>>
    fun flowListLike(term: String): Flow<List<Word>>

    suspend fun listByCategory(category: Int): List<Word>

    suspend fun listByCategoryToLearn(category: Int, count: Int): List<Word>

    suspend fun listByIds(ids: List<Int>): List<Word>

    suspend fun asList(): List<Word>

    suspend fun asListToLearn(count: Int): List<Word>

    suspend fun get(id: Int): Word?

    suspend fun delete(id: Int)

    suspend fun deleteByCategory(category: Int)

    suspend fun save(word: Word): Long

    suspend fun batchSave(words: List<Word>)

    suspend fun update(word: Word)

    suspend fun category(term: String): String?

    fun flowListByCategory(category: Int): Flow<List<Word>>
    fun flowListByCategoryLike(category: Int, term: String): Flow<List<Word>>

    fun flowListByCategorySortByTerm(id: Int, asc: Boolean): Flow<List<Word>>
    fun flowListByCategoryLikeSortByTerm(id: Int, search: String, asc: Boolean): Flow<List<Word>>

    fun flowListByCategorySortByCreated(id: Int, asc: Boolean): Flow<List<Word>>
    fun flowListByCategoryLikeSortByCreated(id: Int, search: String, asc: Boolean): Flow<List<Word>>

    fun flowListByCategorySortByLastRepeated(id: Int, asc: Boolean): Flow<List<Word>>
    fun flowListByCategoryLikeSortByLastRepeated(id: Int, search: String, asc: Boolean): Flow<List<Word>>

    fun listToRepeat(): List<Word>
    fun listByCategoryToRepeat(category: Int): List<Word>

    fun countGrouped(): List<CategoryCount>
    fun countToRepeatGrouped(): List<CategoryCount>
    fun countToLearnGrouped(): List<CategoryCount>
}