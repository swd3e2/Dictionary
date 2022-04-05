package com.dictionary.domain.repository

import com.dictionary.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun list(): List<Category>
    fun create(category: Category): Long
    fun delete(id: Int)
    fun get(id: Int): Category?
}