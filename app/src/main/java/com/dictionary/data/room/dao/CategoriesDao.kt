package com.dictionary.data.room.dao

import androidx.room.*
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Query("select * from ${Category.TABLE_NAME}")
    fun list(): List<Category>

    @Query("select * from ${Category.TABLE_NAME} where id = :id")
    fun get(id: Int): Category?

    @Insert(entity = Category::class, onConflict = OnConflictStrategy.REPLACE)
    fun create(categoryRow: Category): Long

    @Query("delete from ${Category.TABLE_NAME} where id = :id")
    fun delete(id: Int)

    @Transaction
    @Query("select * from ${Category.TABLE_NAME}")
    fun listWithWords(): Flow<List<CategoryWithWords>>

    @Transaction
    @Query("select * from ${Category.TABLE_NAME} where id = :id")
    fun getWithWords(id: Int): CategoryWithWords?

    @Transaction
    @Query("select * from ${Category.TABLE_NAME}")
    fun listWithWordsAsList(): List<CategoryWithWords>

    @Query("select * from ${Category.TABLE_NAME}")
    fun flowList(): Flow<List<Category>>
}