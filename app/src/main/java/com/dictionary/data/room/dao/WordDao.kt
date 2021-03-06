package com.dictionary.data.room.dao

import androidx.room.*
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryCount
import com.dictionary.domain.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE category = :id")
    fun byCategory(id: Int): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE category = :id " +
            "ORDER BY CASE WHEN :isAsc = 1 THEN term END ASC, CASE WHEN :isAsc = 0 THEN term END DESC")
    fun byCategorySortByTerm(id: Int, isAsc: Boolean): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE category = :id " +
            "ORDER BY CASE WHEN :isAsc = 1 THEN created END ASC, CASE WHEN :isAsc = 0 THEN created END DESC")
    fun byCategorySortByCreated(id: Int, isAsc: Boolean): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE category = :id " +
            "ORDER BY CASE WHEN :isAsc = 1 THEN last_repeated END ASC, CASE WHEN :isAsc = 0 THEN last_repeated END DESC")
    fun byCategorySortByLastRepeated(id: Int, isAsc: Boolean): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} " +
            "WHERE category = :id AND (term like '%' || :term || '%' or definition like '%' || :term || '%')")
    fun byCategoryLike(id: Int, term: String): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} " +
            "WHERE category = :id AND (term like '%' || :term || '%' or definition like '%' || :term || '%') " +
            "ORDER BY CASE WHEN :isAsc = 1 THEN term END ASC, CASE WHEN :isAsc = 0 THEN term END DESC")
    fun byCategoryLikeSortByTerm(id: Int, term: String, isAsc: Boolean): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} " +
            "WHERE category = :id AND (term like '%' || :term || '%' or definition like '%' || :term || '%') " +
            "ORDER BY CASE WHEN :isAsc = 1 THEN created END ASC, CASE WHEN :isAsc = 0 THEN created END DESC")
    fun byCategoryLikeSortByCreated(id: Int, term: String, isAsc: Boolean): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} " +
            "WHERE category = :id AND (term like '%' || :term || '%' or definition like '%' || :term || '%') " +
            "ORDER BY CASE WHEN :isAsc = 1 THEN last_repeated END ASC, CASE WHEN :isAsc = 0 THEN last_repeated END DESC")
    fun byCategoryLikeSortByLastRepeated(id: Int, term: String, isAsc: Boolean): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE category = :category")
    fun byCategoryAsList(category: Int): List<Word>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE id = :id")
    fun get(id: Int): Word?

    @Insert(entity = Word::class, onConflict = OnConflictStrategy.REPLACE)
    fun create(word: Word): Long

    @Query("delete FROM ${Word.TABLE_NAME} WHERE id = :id")
    fun delete(id: Int)

    @Query("delete FROM ${Word.TABLE_NAME} WHERE category = :category")
    fun deleteByCategory(category: Int)

    @Update(entity = Word::class)
    fun update(word: Word)

    @Query("SELECT ${Category.TABLE_NAME}.name FROM ${Word.TABLE_NAME} JOIN ${Category.TABLE_NAME} ON ${Category.TABLE_NAME}.id = ${Word.TABLE_NAME}.category WHERE term = :term")
    fun category(term: String): String?

    @Insert(entity = Word::class, onConflict = OnConflictStrategy.REPLACE)
    fun batchCreate(words: List<Word>)

    @Query("SELECT * FROM ${Word.TABLE_NAME}")
    fun asList(): List<Word>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE term like '%' || :term || '%' or definition like '%' || :term || '%'")
    fun listLike(term: String): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME}")
    fun list(): Flow<List<Word>>

    @Query("SELECT * FROM ${Word.TABLE_NAME} where bucket = 0 order by RANDOM() limit :count")
    fun asListToLearn(count: Int): List<Word>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE category = :category AND bucket = 0 order by RANDOM() limit :count")
    fun categoryWordsToLearn(category: Int, count: Int): List<Word>

    @Query(
        "SELECT category as first, count(id) as second FROM ${Word.TABLE_NAME} WHERE bucket > 0 AND bucket < 8 AND " +
            "CASE " +
            "WHEN bucket = 1 THEN last_repeated < :firstBucket " +
            "WHEN bucket = 2 THEN last_repeated < :secondBucket " +
            "WHEN bucket = 3 THEN last_repeated < :thirdBucket " +
            "WHEN bucket = 4 THEN last_repeated < :fourthBucket " +
            "WHEN bucket = 5 THEN last_repeated < :fifthBucket " +
            "WHEN bucket = 6 THEN last_repeated < :sixBucket " +
            "WHEN bucket = 7 THEN last_repeated < :seventhBucket " +
            "END group by category"
    )
    fun countToRepeatGrouped(
        firstBucket: Long,
        secondBucket: Long,
        thirdBucket: Long,
        fourthBucket: Long,
        fifthBucket: Long,
        sixBucket: Long,
        seventhBucket: Long,
    ): List<CategoryCount>

    @Query("SELECT category as first, count(id) as second FROM ${Word.TABLE_NAME} WHERE bucket = 0 group by category")
    fun countToLearnGrouped(): List<CategoryCount>

    @Query(
        "SELECT * FROM ${Word.TABLE_NAME} WHERE bucket > 0 AND bucket < 8 AND " +
            "CASE " +
            "WHEN bucket = 1 THEN last_repeated < :firstBucket " +
            "WHEN bucket = 2 THEN last_repeated < :secondBucket " +
            "WHEN bucket = 3 THEN last_repeated < :thirdBucket " +
            "WHEN bucket = 4 THEN last_repeated < :fourthBucket " +
            "WHEN bucket = 5 THEN last_repeated < :fifthBucket " +
            "WHEN bucket = 6 THEN last_repeated < :sixBucket " +
            "WHEN bucket = 7 THEN last_repeated < :seventhBucket " +
            "END"
    )
    fun listToRepeat(
        firstBucket: Long,
        secondBucket: Long,
        thirdBucket: Long,
        fourthBucket: Long,
        fifthBucket: Long,
        sixBucket: Long,
        seventhBucket: Long,
    ): List<Word>

    @Query(
        "SELECT * FROM ${Word.TABLE_NAME} WHERE bucket > 0 AND bucket < 8 AND category = :category AND " +
            "CASE " +
            "WHEN bucket = 1 THEN last_repeated < :firstBucket " +
            "WHEN bucket = 2 THEN last_repeated < :secondBucket " +
            "WHEN bucket = 3 THEN last_repeated < :thirdBucket " +
            "WHEN bucket = 4 THEN last_repeated < :fourthBucket " +
            "WHEN bucket = 5 THEN last_repeated < :fifthBucket " +
            "WHEN bucket = 6 THEN last_repeated < :sixBucket " +
            "WHEN bucket = 7 THEN last_repeated < :seventhBucket " +
            "END"
    )
    fun categoryListToRepeat(
        category: Int,
        firstBucket: Long,
        secondBucket: Long,
        thirdBucket: Long,
        fourthBucket: Long,
        fifthBucket: Long,
        sixBucket: Long,
        seventhBucket: Long,
    ): List<Word>

    @Query("SELECT * FROM ${Word.TABLE_NAME} WHERE id in (:ids)")
    fun byIds(ids: List<Int>): List<Word>

    @Query("SELECT category as first, count(*) as second FROM ${Word.TABLE_NAME} group by category")
    fun countGrouped(): List<CategoryCount>
}