package com.dictionary.domain.use_case.get_category_words

import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryWordsUseCase @Inject constructor(
    private val rep: WordRepository
){
    operator fun invoke(category: Int): Flow<List<Word>> {
        return rep.categoryWords(category)
    }
}