package com.dictionary.data.repository

import com.dictionary.data.room.dao.WordDao
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val dao: WordDao
): WordRepository {
    override fun categoryWords(category: Int): Flow<List<Word>> {
        return dao.byCategory(category)
    }

    override fun get(id: Int): Word? {
        return dao.get(id)
    }

    override fun delete(id: Int) {
        dao.delete(id)
    }

    override fun create(word: Word) {
        dao.create(word)
    }
}