package com.dictionary.data.repository

import com.dictionary.data.room.dao.WordDao
import com.dictionary.domain.entity.CategoryCount
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.utils.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val dao: WordDao
): WordRepository {
    override fun flowListByCategory(category: Int): Flow<List<Word>> {
        return dao.byCategory(category)
    }

    override fun flowListByCategorySortByTerm(id: Int, asc: Boolean): Flow<List<Word>> {
        return dao.byCategorySortByTerm(id, asc)
    }

    override fun flowListByCategorySortByCreated(id: Int, asc: Boolean): Flow<List<Word>> {
        return dao.byCategorySortByCreated(id, asc)
    }

    override fun flowListByCategorySortByLastRepeated(id: Int, asc: Boolean): Flow<List<Word>> {
        return dao.byCategorySortByLastRepeated(id, asc)
    }

    override fun flowListByCategoryLike(category: Int, term: String): Flow<List<Word>> {
        return dao.byCategoryLike(category, term)
    }

    override fun flowListByCategoryLikeSortByTerm(id: Int, search: String, asc: Boolean): Flow<List<Word>> {
        return dao.byCategoryLikeSortByTerm(id, search, asc)
    }

    override fun flowListByCategoryLikeSortByCreated(id: Int, search: String, asc: Boolean): Flow<List<Word>> {
        return dao.byCategoryLikeSortByCreated(id, search, asc)
    }

    override fun flowListByCategoryLikeSortByLastRepeated(id: Int, search: String, asc: Boolean): Flow<List<Word>> {
        return dao.byCategoryLikeSortByLastRepeated(id, search, asc)
    }

    override fun countGrouped(): List<CategoryCount> {
        return dao.countGrouped()
    }

    override fun countToLearnGrouped(): List<CategoryCount> {
        return dao.countToLearnGrouped()
    }

    override fun countToRepeatGrouped():List<CategoryCount> {
        val currentDate = Date().toInstant().toEpochMilli()
        return dao.countToRepeatGrouped(
            currentDate - Day,
            currentDate - Day2,
            currentDate - Day3,
            currentDate - Day7,
            currentDate - Day14,
            currentDate - Day30,
            currentDate - Day90,
        )
    }

    override fun listToRepeat(): List<Word> {
        val currentDate = Date().toInstant().toEpochMilli()
        return dao.listToRepeat(
            currentDate - Day,
            currentDate - Day2,
            currentDate - Day3,
            currentDate - Day7,
            currentDate - Day14,
            currentDate - Day30,
            currentDate - Day90,
        )
    }

    override fun listByCategoryToRepeat(category: Int): List<Word> {
        val currentDate = Date().toInstant().toEpochMilli()
        return dao.categoryListToRepeat(
            category,
            currentDate - Day,
            currentDate - Day2,
            currentDate - Day3,
            currentDate - Day7,
            currentDate - Day14,
            currentDate - Day30,
            currentDate - Day90,
        )
    }

    override fun flowList(): Flow<List<Word>> {
        return dao.list()
    }

    override fun flowListLike(term: String): Flow<List<Word>> {
        return dao.listLike(term)
    }

    override suspend fun listByCategory(category: Int): List<Word> {
        return dao.byCategoryAsList(category)
    }

    override suspend fun listByCategoryToLearn(category: Int, count: Int): List<Word> {
        return dao.categoryWordsToLearn(category, count)
    }

    override suspend fun listByIds(ids: List<Int>): List<Word> {
        return dao.byIds(ids)
    }

    override suspend fun asList(): List<Word> {
        return dao.asList()
    }

    override suspend fun asListToLearn(count: Int): List<Word> {
        return dao.asListToLearn(count)
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

    override suspend fun save(word: Word): Long {
        return dao.create(word)
    }

    override suspend fun batchSave(words: List<Word>) {
        return dao.batchCreate(words)
    }

    override suspend fun update(word: Word) {
        dao.update(word)
    }

    override suspend fun category(term: String): String? {
        return dao.category(term)
    }
}