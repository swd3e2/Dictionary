package com.dictionary.presentation.category_edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.common.Resource
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.TranslationRepository
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.category_list.CategoryListEvent
import com.dictionary.utils.Routes
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val wordRepository: WordRepository,
    private val translationRepository: TranslationRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var showDeleteDialog = mutableStateOf(false)
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var category: Category = Category(0, "")
        private set

    var openDialog = mutableStateOf(false)
        private set

    var wordTranslations = mutableStateListOf<String>()
        private set

    var termSearch = mutableStateOf("")
        private set

    private var words =  MutableStateFlow("")
    private var selectedWordId: Int? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    var wordsState = words.flatMapLatest {
        if (it.isEmpty()) {
            wordRepository.categoryWords(category.id)
        } else {
            wordRepository.categoryWordsLike(category.id, it)
        }
    }

    var wordsCount = mutableStateOf(0)
        private set

    var newWordTerm = mutableStateOf("test")
        private set

    var newWordDefinition = mutableStateOf("")
        private set

    var menuExpanded = mutableStateOf(false)
        private set

    var wordWithTermExists = mutableStateOf(false)
        private set

    private val _state = mutableStateOf(WordTranslationState(translation = null, isLoading = false))
    val state: State<WordTranslationState> = _state

    private var searchJob: Job? = null

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if(id != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                categoryRepository.get(id)?.let { c ->
                    category = c
                }
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
            is CategoryEditEvent.OnTermChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    wordWithTermExists.value = wordRepository.exists(event.term)
                }
                newWordTerm.value = event.term
            }
            is CategoryEditEvent.OnDefinitionChange -> {
                newWordDefinition.value = event.definition
            }
            is CategoryEditEvent.OnDeleteWord -> {
                viewModelScope.launch(Dispatchers.IO) {
                    selectedWordId?.let {
                        wordRepository.delete(it)
                    }
                    showDeleteDialog.value = false
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
            is CategoryEditEvent.OnWordSaveClick -> {
                if (newWordTerm.value.isEmpty() || (newWordDefinition.value.isEmpty() && wordTranslations.size == 0)) {
                    return
                }

                var definition = ""
                if (!newWordDefinition.value.isEmpty()) {
                    definition = newWordDefinition.value
                } else {
                    for (translation in wordTranslations) {
                        definition += "$translation, "
                    }
                    definition = definition.dropLast(2)
                }

                viewModelScope.launch(Dispatchers.IO) {
                    wordRepository.create(Word(
                        id = 0,
                        term = newWordTerm.value,
                        definition = definition,
                        category = category.id,
                        created = Date(),
                        lastRepeated = Date(),
                    ))

                    newWordTerm.value = ""
                    newWordDefinition.value = ""
                    openDialog.value = false
                    _state.value = WordTranslationState(translation = null)
                    wordTranslations.clear()
                }
            }
            is CategoryEditEvent.OnOpenAddWordDialog -> {
                openDialog.value = true
                wordWithTermExists.value = false
            }
            is CategoryEditEvent.OnCloseAddWordDialog -> {
                newWordTerm.value = ""
                newWordDefinition.value = ""
                openDialog.value = false
                _state.value = WordTranslationState(translation = null)
                wordTranslations.clear()
            }
            is CategoryEditEvent.OnSearchTermChange -> {
                termSearch.value = event.term
                words.value = event.term
            }
            is CategoryEditEvent.GetTranslation -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    translationRepository.getTranslation(newWordTerm.value)
                        .onEach { result ->
                            when(result) {
                                is Resource.Success -> {
                                    _state.value = state.value.copy(
                                        translation = result.data,
                                        isLoading = false
                                    )
                                }
                                is Resource.Error -> {
                                    _state.value = state.value.copy(
                                        isLoading = false
                                    )
                                }
                                is Resource.Loading -> {
                                    _state.value = state.value.copy(
                                        isLoading = true
                                    )
                                }
                            }
                        }.launchIn(this)
                }
            }
            is CategoryEditEvent.OnShowDeleteDialog -> {
                showDeleteDialog.value = true
                selectedWordId = event.id
            }
            is CategoryEditEvent.OnHideDeleteDialog -> {
                showDeleteDialog.value = false
            }
        }
    }
}