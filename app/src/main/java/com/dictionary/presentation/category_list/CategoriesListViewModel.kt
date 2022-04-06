package com.dictionary.presentation.category_list

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.WordRepository
import com.dictionary.utils.Routes
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val app: Application,
    private val categoryRepository: CategoryRepository,
    private val wordsRepository: WordRepository
) : AndroidViewModel(app) {

    val filename: MutableLiveData<Uri> by lazy { MutableLiveData<Uri>() }

    var openDialog = mutableStateOf(false)
        private set

    var title = mutableStateOf("")
        private set

    val categories = mutableStateListOf<CategoryWithWords>()

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        categories.addAll(categoryRepository.listWithWords())
    }

    fun onEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.OnSaveClick -> {
                if (title.value.isEmpty()) {
                    return
                }
                val newCategory = Category(id = 0, name = title.value)
                val newCategoryId = categoryRepository.create(newCategory)
                newCategory.id = newCategoryId.toInt()

                title.value = ""
                openDialog.value = false
                categories.add(CategoryWithWords(category = newCategory, words = emptyList()))
            }
            is CategoryListEvent.OnChangeTitle -> {
                title.value = event.title
            }
            is CategoryListEvent.OnDeleteCategory -> {
                categoryRepository.delete(event.category.category.id)
                wordsRepository.deleteByCategory(event.category.category.id)
                categories.remove(event.category)
            }
            is CategoryListEvent.OnCategoryClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.CATEGORY_EDIT + "?id=${event.id}"))
            }
            is CategoryListEvent.OnGameClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.CARDS_GAME + "?id=${event.id}"))
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

    fun onFilenameChange(uri: Uri) {
        val readBytes = app.contentResolver.openInputStream(uri)!!.readBytes()
        val data = readBytes.toString(Charsets.UTF_8)

        println("FILENAME $data")
        println("FILENAME ${getFilename(uri)}")

        val words = mutableListOf<Word>()
        for (line in data.lines()) {
            val splitLine = line.split(';')
            if (splitLine.size < 2) {
                continue
            }

            words.add(Word(
                id = 0,
                term = splitLine[0],
                definition = splitLine[1],
                category = 0,
                created = Date(),
                lastRepeated = Date(),
            ))
        }

        if (words.size == 0) {
            return
        }

        val categoryId = categoryRepository.create(Category(id = 0, name = getFilename(uri)))

        for (word in words) {
            word.category = categoryId.toInt()
            wordsRepository.create(word)
        }

        categories.clear()
        categories.addAll(categoryRepository.listWithWords())
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun getFilename(uri: Uri): String {
        val path = uri.path
        val splitPath = path?.split('/')
        if (splitPath == null || splitPath.isEmpty()) {
            return ""
        }

        val splitFileExtension = splitPath[splitPath.size-1].split('.')
        if (splitFileExtension.size < 2) {
            return ""
        }

        return splitFileExtension[0]
    }
}