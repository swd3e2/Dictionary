package com.dictionary.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("select * from ${Word.TABLE_NAME} where category = :category")
    fun byCategory(category: Int): Flow<List<Word>>

    @Query("select * from ${Word.TABLE_NAME} where id = :id")
    fun get(id: Int): Word?

    @Insert(entity = Word::class, onConflict = OnConflictStrategy.REPLACE)
    fun create(word: Word): Long

    @Query("delete from ${Word.TABLE_NAME} where id = :id")
    fun delete(id: Int)
}