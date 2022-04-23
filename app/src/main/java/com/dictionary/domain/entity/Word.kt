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
    var created: Date = Date(),
    @ColumnInfo(name = "last_repeated")
    var lastRepeated: Date? = null,
    @ColumnInfo(name = "first_learned")
    var firstLearned: Date? = null,
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
            1 -> return lastRepeated != null && Date(lastRepeated!!.time + Day).before(currentDate)
            2 -> return lastRepeated != null && Date(lastRepeated!!.time + Day2).before(currentDate)
            3 -> return lastRepeated != null && Date(lastRepeated!!.time + Day3).before(currentDate)
            4 -> return lastRepeated != null && Date(lastRepeated!!.time + Day7).before(currentDate)
            5 -> return lastRepeated != null && Date(lastRepeated!!.time + Day14).before(currentDate)
            6 -> return lastRepeated != null && Date(lastRepeated!!.time + Day30).before(currentDate)
            7 -> return lastRepeated != null && Date(lastRepeated!!.time + Day90).before(currentDate)
        }

        return false
    }
}