package com.dictionary.domain.use_case.create_word

import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import javax.inject.Inject

class CreateWordUseCase @Inject constructor(
    private val rep: WordRepository
){
    operator fun invoke(word: Word) {
        rep.create(word)
    }
}