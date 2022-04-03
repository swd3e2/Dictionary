package com.dictionary.presentation.category_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Category
import com.dictionary.domain.use_case.create_category.CreateCategoryUseCase
import com.dictionary.domain.use_case.delete_category.DeleteCategoryUseCase
import com.dictionary.domain.use_case.get_categories_list.GetCategoriesListUseCase
import com.dictionary.utils.Routes
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesListUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
) : ViewModel() {

    var openDialog = mutableStateOf(false)
        private set

    var title = mutableStateOf("")
        private set

    val categories = getCategoriesUseCase()

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CategoryListEvent): Unit {
        when (event) {
            is CategoryListEvent.OnSaveClick -> {
                if (title.value.isEmpty()) {
                    return
                }
                createCategoryUseCase(Category(id = null, name = title.value))
                title.value = ""
                openDialog.value = false
            }
            is CategoryListEvent.OnChangeTitle -> {
                title.value = event.title
            }
            is CategoryListEvent.OnDeleteCategory -> {
                deleteCategoryUseCase(event.id)
            }
            is CategoryListEvent.OnCategoryClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.CATEGORY_EDIT + "?id=${event.id}"))
            }
            is CategoryListEvent.OnOpenAddCategoryDialog -> {
                openDialog.value = true
            }
            is CategoryListEvent.OnCloseAddCategoryDialog -> {
                title.value = ""
                openDialog.value = false
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}