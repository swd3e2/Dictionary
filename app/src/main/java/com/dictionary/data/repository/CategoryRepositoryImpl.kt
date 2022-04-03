package com.dictionary.data.repository

import android.util.Log
import com.dictionary.data.room.dao.CategoriesDao
import com.dictionary.domain.entity.Category
import com.dictionary.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoriesDao
) : CategoryRepository {

    override fun list(): Flow<List<Category>> {
        return dao.list()
    }

    override fun create(category: Category): Unit {
        val id = dao.create(Category(category.id, category.name))
        Log.v("MyActivity", "id inserted $id");
        list()
    }

    override fun delete(id: Int) {
        println("asd $id")
        dao.delete(id)
    }

    override fun get(id: Int): Category? {
        return dao.get(id)
    }

}