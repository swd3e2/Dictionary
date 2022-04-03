package com.dictionary.domain.repository

import com.dictionary.common.Resource
import com.dictionary.domain.entity.Translation
import kotlinx.coroutines.flow.Flow

interface TranslationRepository {
    fun getTranslation(word: String): Flow<Resource<Translation>>
}