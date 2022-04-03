package com.dictionary.domain.use_case.delete_wrod

import com.dictionary.domain.repository.WordRepository
import javax.inject.Inject

class DeleteWordUseCase @Inject constructor(
    private val rep: WordRepository
){
    operator fun invoke(id: Int) {
        rep.delete(id)
    }
}