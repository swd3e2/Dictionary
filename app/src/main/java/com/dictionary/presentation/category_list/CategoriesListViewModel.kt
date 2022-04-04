package com.dictionary.presentation.category_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Category
import com.dictionary.domain.repository.CategoryRepository
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
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val filename: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    var openDialog = mutableStateOf(false)
        private set

    var title = mutableStateOf("")
        private set

    val categories = mutableStateOf(emptyList<Category>())

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        categories.value = categoryRepository.list()
    }

    fun onEvent(event: CategoryListEvent): Unit {
        when (event) {
            is CategoryListEvent.OnSaveClick -> {
                if (title.value.isEmpty()) {
                    return
                }
                categoryRepository.create(Category(id = null, name = title.value))
                title.value = ""
                openDialog.value = false
                categories.value = categoryRepository.list()
            }
            is CategoryListEvent.OnChangeTitle -> {
                title.value = event.title
            }
            is CategoryListEvent.OnDeleteCategory -> {
                categoryRepository.delete(event.id)
                categories.value = categoryRepository.list()
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

    fun onFilenameChange(filename: String): Unit {
        println("NEWNAME: $filename")
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}