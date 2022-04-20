package com.dictionary.presentation.search_words

import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.R
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.utils.Routes
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchWordsViewModel @Inject constructor(
    categoryRepository: CategoryRepository,
    val wordRepository: WordRepository,
): ViewModel() {
    var termSearch = mutableStateOf("")
        private set
    var showWordDeleteDialog = mutableStateOf(false)
        private set
    var showSortDialog = mutableStateOf(false)
        private set
    var showMoveToCategoryDialog = mutableStateOf(false)
        private set

    private var words = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    var wordsState = words.flatMapLatest {
        if (it.isEmpty()) {
            wordRepository.list()
        } else {
            wordRepository.listLike(it)
        }
    }

    var categories: List<Category> = emptyList()
    var bitmapsMap = mutableMapOf<Int, ImageBitmap>()

    private var selectedWord: Word? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val dbCategories = categoryRepository.list()
            withContext(Dispatchers.Main) {
                categories = dbCategories

                for (category in categories) {
                    if (category.image.isEmpty()) {
                        continue
                    }

                    bitmapsMap[category.id] = BitmapFactory.decodeFile(category.image).asImageBitmap()
                }
            }
        }
    }

    fun onEvent(event: SearchWordsEvent) {
        when (event) {
            SearchWordsEvent.OnHideSortDialog -> {
                showSortDialog.value = false
            }
            is SearchWordsEvent.OnSearchTermChange -> {
                termSearch.value = event.term
                words.value = event.term
            }
            SearchWordsEvent.OnShowSortDialog -> {
                showSortDialog.value = true
            }
            is SearchWordsEvent.OnSortChange -> {

            }
            is SearchWordsEvent.OnDropWordBucket -> {
                viewModelScope.launch(Dispatchers.IO) {
                    wordRepository.create(event.word.apply { bucket = 1 })
                    _uiEvent.send(UiEvent.ShowSnackbar("Word sent to bucket 1"))
                }
            }
            is SearchWordsEvent.OnShowWordDeleteDialog -> {
                showWordDeleteDialog.value = true
                selectedWord = event.word
            }
            is SearchWordsEvent.OnHideWordDeleteDialog -> {
                showWordDeleteDialog.value = false
            }
            is SearchWordsEvent.OnDeleteWord -> {
                viewModelScope.launch(Dispatchers.IO) {
                    selectedWord?.let {
                        wordRepository.delete(it.id)
                        _uiEvent.send(UiEvent.ShowSnackbar("Word `${it.term}` deleted"))
                    }
                    showWordDeleteDialog.value = false
                }
            }
            is SearchWordsEvent.OnWordClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(Routes.WORD_EDIT + "?id=${event.id}"))
                }
            }
            is SearchWordsEvent.OnShowMoveWordToCategoryDialog -> {
                selectedWord = event.word
                showMoveToCategoryDialog.value = true
            }
            is SearchWordsEvent.OnHideMoveWordToCategoryDialog -> {
                showMoveToCategoryDialog.value = false
            }
            is SearchWordsEvent.OnMoveWordToCategory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    wordRepository.create(selectedWord!!.apply { category = event.category.id })
                    _uiEvent.send(UiEvent.ShowSnackbar("Word moved to category ${event.category.name}"))
                }
                showMoveToCategoryDialog.value = false
            }
        }
    }
}