package com.dictionary.domain.repository

import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun flowListWithWords(): Flow<List<CategoryWithWords>>
    fun flowList(): Flow<List<Category>>
    suspend fun listWithWords(): List<CategoryWithWords>
    suspend fun list(): List<Category>
    suspend fun create(category: Category): Long
    suspend fun delete(id: Int)
    suspend fun get(id: Int): Category?
    suspend fun getWithWords(id: Int): CategoryWithWords?
}