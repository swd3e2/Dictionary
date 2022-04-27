package com.dictionary.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dictionary.data.room.converter.Converters
import com.dictionary.data.room.dao.CategoriesDao
import com.dictionary.data.room.dao.WordDao
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.Word

@Database(
    entities = [
        Category::class,
        Word::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun categoryDao(): CategoriesDao
    abstract fun wordDao(): WordDao
}