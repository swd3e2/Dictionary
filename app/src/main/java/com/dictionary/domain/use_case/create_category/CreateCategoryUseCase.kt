package com.dictionary.domain.use_case.create_category

import com.dictionary.domain.entity.Category
import com.dictionary.domain.repository.CategoryRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val rep: CategoryRepository
){
    operator fun invoke(category: Category): Unit {
        rep.create(category)
    }
}