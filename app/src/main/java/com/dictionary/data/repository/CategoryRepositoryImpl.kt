package com.dictionary.data.repository

import com.dictionary.data.room.dao.CategoriesDao
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoriesDao
) : CategoryRepository {
    override fun listWithWords(): Flow<List<CategoryWithWords>> {
        return dao.listWithWords()
    }

    override suspend fun list(): List<Category> {
        return dao.list()
    }

    override suspend fun create(category: Category): Long {
        return dao.create(Category(category.id, category.name))
    }

    override suspend fun delete(id: Int) {
        dao.delete(id)
    }

    override suspend fun get(id: Int): Category? {
        return dao.get(id)
    }

}