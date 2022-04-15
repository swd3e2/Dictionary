package com.dictionary.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dictionary.presentation.utils.*
import java.util.*

@Entity(tableName = Word.TABLE_NAME)
data class Word(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "term")
    var term: String = "",
    @ColumnInfo(name = "definition")
    var definition: String = "",
    @ColumnInfo(name = "category")
    var category: Int? = null,
    @ColumnInfo(name = "created")
    val created: Date,
    @ColumnInfo(name = "last_repeated")
    var lastRepeated: Date,
    @ColumnInfo(name = "bucket")
    var bucket: Int = 0,
    @ColumnInfo(name = "synonyms")
    var synonyms: String = "",
    @ColumnInfo(name = "antonyms")
    var antonyms: String = "",
    @ColumnInfo(name = "transcription")
    var transcription: String = "",
    @ColumnInfo(name = "similar")
    var similar: String = "",
) {
    companion object {
        const val TABLE_NAME = "words"
    }

    fun shouldBeRepeated(): Boolean {
        val currentDate = Date()
        when (bucket) {
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