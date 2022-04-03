package com.dictionary.domain.use_case.get_category

import com.dictionary.domain.entity.Category
import com.dictionary.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val rep: CategoryRepository
){
    operator fun invoke(id: Int): Category? {
        return rep.get(id)
    }
}