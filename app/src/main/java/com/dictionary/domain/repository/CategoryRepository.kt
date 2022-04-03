package com.dictionary.domain.repository

import com.dictionary.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun list(): Flow<List<Category>>
    fun create(category: Category): Unit
    fun delete(id: Int): Unit
    fun get(id: Int): Category?
}