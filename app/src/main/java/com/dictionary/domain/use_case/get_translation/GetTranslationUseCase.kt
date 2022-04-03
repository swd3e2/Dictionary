package com.dictionary.domain.use_case.get_translation

import com.dictionary.common.Resource
import com.dictionary.domain.entity.Translation
import com.dictionary.domain.repository.TranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTranslationUseCase @Inject constructor(
    private val repository: TranslationRepository
) {
    operator fun invoke(word: String): Flow<Resource<Translation>> {
        if(word.isBlank()) {
            return flow {  }
        }
        return repository.getTranslation(word)
    }
}