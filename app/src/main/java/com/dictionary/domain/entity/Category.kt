package com.dictionary.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Category.TABLE_NAME)
class Category (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id")
    var id: Int,
    @ColumnInfo(name="name")
    val name: String,
){
    companion object {
        const val TABLE_NAME = "categories"
    }
}