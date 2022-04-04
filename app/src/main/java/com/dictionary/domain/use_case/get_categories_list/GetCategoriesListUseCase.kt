package com.dictionary.domain.use_case.get_categories_list

import com.dictionary.domain.entity.Category
import com.dictionary.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesListUseCase @Inject constructor(
    private val rep: CategoryRepository
){
    operator fun invoke(): List<Category> {
        return rep.list()
    }
}