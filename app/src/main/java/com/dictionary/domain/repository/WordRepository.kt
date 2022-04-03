package com.dictionary.domain.repository

import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun categoryWords(category: Int): Flow<List<Word>>
    fun get(id: Int): Word?
    fun delete(id: Int)
    fun create(word: Word)
}