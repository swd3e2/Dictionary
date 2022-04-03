package com.dictionary.domain.use_case.delete_category

import com.dictionary.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val rep: CategoryRepository
) {
    operator fun invoke(id: Int): Unit {
        rep.delete(id)
    }
}