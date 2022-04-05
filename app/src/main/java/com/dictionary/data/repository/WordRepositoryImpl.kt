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

    override fun categoryWordsAsList(category: Int): List<Word> {
        return dao.byCategoryAsList(category)
    }

    override fun get(id: Int): Word? {
        return dao.get(id)
    }

    override fun delete(id: Int) {
        dao.delete(id)
    }

    override fun deleteByCategory(category: Int) {
        dao.deleteByCategory(category)
    }

    override fun create(word: Word): Long {
        return dao.create(word)
    }

    override fun update(word: Word) {
        dao.update(word)
    }
}