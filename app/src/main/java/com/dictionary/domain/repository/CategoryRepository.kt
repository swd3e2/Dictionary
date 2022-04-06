package com.dictionary.domain.repository

import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun listWithWords(): List<CategoryWithWords>
    fun list(): List<Category>
    fun create(category: Category): Long
    fun delete(id: Int)
    fun get(id: Int): Category?
}