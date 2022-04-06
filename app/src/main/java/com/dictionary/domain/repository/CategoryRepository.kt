package com.dictionary.domain.repository

import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun listWithWords(): Flow<List<CategoryWithWords>>
    suspend fun list(): List<Category>
    suspend fun create(category: Category): Long
    suspend fun delete(id: Int)
    suspend fun get(id: Int): Category?
}