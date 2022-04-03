package com.dictionary.data.room.dao

import androidx.room.*
import com.dictionary.domain.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Query("select * from ${Category.TABLE_NAME}")
    fun list(): Flow<List<Category>>

    @Query("select * from ${Category.TABLE_NAME} where id = :id")
    fun get(id: Int): Category?

    @Insert(entity = Category::class, onConflict = OnConflictStrategy.REPLACE)
    fun create(categoryRow: Category): Long

    @Query("delete from ${Category.TABLE_NAME} where id = :id")
    fun delete(id: Int)
}