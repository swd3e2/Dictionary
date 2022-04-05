package com.dictionary.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dictionary.presentation.utils.*
import java.util.*

@Entity(tableName = Word.TABLE_NAME)
data class Word(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "term")
    val term: String,
    @ColumnInfo(name = "definition")
    val definition: String,
    @ColumnInfo(name = "category")
    var category: Int?,
    @ColumnInfo(name = "created")
    val created: Date,
    @ColumnInfo(name = "last_repeated")
    var lastRepeated: Date,
    @ColumnInfo(name = "bucket")
    var bucket: Int = 0,
    @ColumnInfo(name = "synonyms")
    val synonyms: String = "",
    @ColumnInfo(name = "antonyms")
    val antonyms: String = "",
    @ColumnInfo(name = "transcription")
    val transcription: String = "",
    @ColumnInfo(name = "similar")
    val similar: String = "",
) {
    companion object {
        const val TABLE_NAME = "words"
    }

    fun shouldBeLearned(): Boolean {
        val currentDate = Date()
        when (bucket) {
            0 -> {
                return true
            }
            1 -> {
                if (Date(lastRepeated.time + Day).before(currentDate)) {
                    return true
                }
            }
            2 -> {
                if (Date(lastRepeated.time + Day2).before(currentDate)) {
                    return true
                }
            }
            3 -> {
                if (Date(lastRepeated.time + Day3).before(currentDate)) {
                    return true
                }
            }
            4 -> {
                if (Date(lastRepeated.time + Day7).before(currentDate)) {
                    return true
                }
            }
            5 -> {
                if (Date(lastRepeated.time + Day14).before(currentDate)) {
                    return true
                }
            }
            6 -> {
                if (Date(lastRepeated.time + Day30).before(currentDate)) {
                    return true
                }
            }
            7 -> {
                if (Date(lastRepeated.time + Day90).before(currentDate)) {
                    return true
                }
            }
        }

        return false
    }
}