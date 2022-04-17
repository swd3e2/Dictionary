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

    override fun categoryWordsLike(category: Int, term: String): Flow<List<Word>> {
        return dao.byCategoryLike(category, term)
    }

    override suspend fun categoryWordsAsList(category: Int): List<Word> {
        return dao.byCategoryAsList(category)
    }

    override suspend fun asList(): List<Word> {
        return dao.asList()
    }

    override suspend fun get(id: Int): Word? {
        return dao.get(id)
    }

    override suspend fun delete(id: Int) {
        dao.delete(id)
    }

    override suspend fun deleteByCategory(category: Int) {
        dao.deleteByCategory(category)
    }

    override suspend fun create(word: Word): Long {
        return dao.create(word)
    }

    override suspend fun batchCreate(words: List<Word>) {
        return dao.batchCreate(words)
    }

    override suspend fun update(word: Word) {
        dao.update(word)
    }

    override suspend fun exists(term: String): Boolean {
        return dao.exists(term)
    }
}