package com.dictionary.presentation.category_edit

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.category_list.CategoryListEvent
import com.dictionary.utils.Routes
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val wordsRepository: WordRepository,

    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    var categoryTitle = mutableStateOf("")
        private set
    var categoryImage = mutableStateOf("")
        private set

    var showWordDeleteDialog = mutableStateOf(false)
        private set

    var showCategoryDeleteDialog = mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var category: Category = Category(0, "")
        private set

    var showSortDialog = mutableStateOf(false)
        private set

    var showMoveToCategoryDialog = mutableStateOf(false)
        private set

    var wordTranslations = mutableStateListOf<String>()
        private set

    var termSearch = mutableStateOf("")
        private set

    lateinit var categories: List<Category>

    private var words = MutableStateFlow("")
    private var selectedWord: Word? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    var wordsState = words.flatMapLatest {
        if (it.isEmpty()) {
            wordsRepository.categoryWords(category.id)
        } else {
            wordsRepository.categoryWordsLike(category.id, it)
        }
    }

    var wordsCount = mutableStateOf(0)
        private set

    var menuExpanded = mutableStateOf(false)
        private set

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if (id != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                categoryRepository.get(id)?.let { c ->
                    category = c

                    withContext(Dispatchers.Main) {
                        categoryTitle.value = c.name
                        categoryImage.value = c.image
                    }
                }
                categories = categoryRepository.list().toCollection(mutableListOf()).filter { it.id != category.id }
            }
        }
    }

    fun onEvent(event: CategoryEditEvent) {
        when (event) {
            is CategoryEditEvent.OnMenuClick -> {
                menuExpanded.value = true
            }
            is CategoryEditEvent.OnCloseMenu -> {
                menuExpanded.value = false
            }
            is CategoryEditEvent.OnDeleteWord -> {
                viewModelScope.launch(Dispatchers.IO) {
                    selectedWord?.let {
                        wordsRepository.delete(it.id)
                    }
                    showWordDeleteDialog.value = false
                }
            }
            is CategoryEditEvent.OnGameClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(Routes.CARDS_GAME + "?id=${event.id}"))
                }
            }
            is CategoryEditEvent.OnLearnClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(Routes.LEARN_WORDS + "?id=${event.id}"))
                }
            }
            is CategoryEditEvent.OnWordClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(Routes.WORD_EDIT + "?id=${event.id}"))
                }
            }
            is CategoryEditEvent.OnSearchTermChange -> {
                termSearch.value = event.term
                words.value = event.term
            }
            is CategoryEditEvent.OnShowWordDeleteDialog -> {
                showWordDeleteDialog.value = true
                selectedWord = event.word
            }
            is CategoryEditEvent.OnHideWordDeleteDialog -> {
                showWordDeleteDialog.value = false
            }
            is CategoryEditEvent.OnTitleSave -> {
                if (categoryTitle.value.isEmpty()) {
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    category.name = categoryTitle.value
                    categoryRepository.create(category)
                    _uiEvent.send(UiEvent.ShowSnackbar("Title changed"))
                }
            }
            is CategoryEditEvent.OnDropWordBucket -> {
                viewModelScope.launch(Dispatchers.IO) {
                    wordsRepository.create(event.word.apply { bucket = 1 })
                    _uiEvent.send(UiEvent.ShowSnackbar("Word sent to bucket 1"))
                }
            }
            is CategoryEditEvent.OnTitleChange -> {
                categoryTitle.value = event.title
            }
            is CategoryEditEvent.OnShowSortDialog -> {
                showSortDialog.value = true
            }
            is CategoryEditEvent.OnHideSortDialog -> {
                showSortDialog.value = false
            }
            is CategoryEditEvent.OnSortChange -> {
                showSortDialog.value = false
            }
            is CategoryEditEvent.OnShowMoveToCategoryDialog -> {
                selectedWord = event.word
                showMoveToCategoryDialog.value = true
            }
            is CategoryEditEvent.OnHideMoveToCategoryDialog -> {
                showMoveToCategoryDialog.value = false
            }
            is CategoryEditEvent.OnMoveToCategoryDialog -> {
                viewModelScope.launch(Dispatchers.IO) {
                    wordsRepository.create(selectedWord!!.apply { category = event.category.id })
                }
                showMoveToCategoryDialog.value = false
            }
            is CategoryEditEvent.OnImagePickFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val app = getApplication<Application>()

                    val wrapper = ContextWrapper(app.applicationContext)
                    val folder = wrapper.getDir("images", Context.MODE_PRIVATE)
                    val currentFile = File(categoryImage.value)

                    if (categoryImage.value.isNotEmpty() && currentFile.exists()) {
                        try {
                            currentFile.delete()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    val filename = "${UUID.randomUUID()}.jpg"
                    val file = File(folder, filename)
                    try {
                        val stream: OutputStream = FileOutputStream(file)
                        app.contentResolver.openInputStream(event.uri)!!.copyTo(stream)
                        stream.flush()
                        stream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    category.image = file.path
                    categoryImage.value = file.path
                    categoryRepository.create(category = category)
                }
            }
            is CategoryEditEvent.OnAddWord -> viewModelScope.launch {
                _uiEvent.send(UiEvent.Navigate(Routes.WORD_EDIT + "?category=${category.id}"))
            }
            is CategoryEditEvent.OnShowDeleteCategoryDialog -> {
                showCategoryDeleteDialog.value = true
                menuExpanded.value = false
            }
            is CategoryEditEvent.OnHideDeleteCategoryDialog -> {
                showCategoryDeleteDialog.value = false
                menuExpanded.value = false
            }
            is CategoryEditEvent.OnDeleteCategory -> {
                showCategoryDeleteDialog.value = false
                viewModelScope.launch(Dispatchers.IO) {
                    categoryRepository.delete(category.id)
                    wordsRepository.deleteByCategory(category.id)
                    if (category.image.isNotEmpty()) {
                        try {
                            val currentFile = File(category.image)
                            currentFile.exists() && currentFile.delete()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    _uiEvent.send(UiEvent.PopBackStack)
                }
            }
        }
    }
}