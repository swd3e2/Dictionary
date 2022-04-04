package com.dictionary.data.repository

import com.dictionary.data.room.dao.CategoriesDao
import com.dictionary.domain.entity.Category
import com.dictionary.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoriesDao
) : CategoryRepository {

    override fun list(): List<Category> {
        return dao.list()
    }

    override fun create(category: Category): Unit {
        val id = dao.create(Category(category.id, category.name))
        list()
    }

    override fun delete(id: Int) {
        dao.delete(id)
    }

    override fun get(id: Int): Category? {
        return dao.get(id)
    }

}