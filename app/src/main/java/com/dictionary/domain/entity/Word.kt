package com.dictionary.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = Word.TABLE_NAME)
data class Word(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id")
    val id: Int,
    @ColumnInfo(name="term")
    val term: String,
    @ColumnInfo(name="definition")
    val definition: String,
    @ColumnInfo(name="category")
    val category: Int?,
    @ColumnInfo(name="created")
    val created: Date,
    @ColumnInfo(name="last_repeated")
    val lastRepeated: Date,
) {
    companion object {
        const val TABLE_NAME = "words"
    }
}