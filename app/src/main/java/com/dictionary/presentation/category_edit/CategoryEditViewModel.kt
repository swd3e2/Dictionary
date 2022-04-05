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
import com.dictionary.utils.Routes
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var openDialog = mutableStateOf(false)
        private set

    var wordTranslations = mutableStateListOf<String>()
        private set

    var words: Flow<List<Word>>? = null

    var category: Category = Category(0, "")
        private set

    var newWordTerm = mutableStateOf("test")
        private set

    var newWordDefinition = mutableStateOf("")
        private set

    var menuExpanded = mutableStateOf(false)
        private set

    private val _state = mutableStateOf(WordTranslationState(translation = null, isLoading = false))
    val state: State<WordTranslationState> = _state

    private var searchJob: Job? = null

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if(id != -1) {
            viewModelScope.launch {
                categoryRepository.get(id)?.let { c ->
                    category = c
                    words = wordRepository.categoryWords(c.id!!)
                }
            }
        }
    }

    fun onEvent(event: CategoryEditEvent): Unit {
        when (event) {
            is CategoryEditEvent.OnMenuClick -> {
                menuExpanded.value = true
            }
            is CategoryEditEvent.OnCloseMenu -> {
                menuExpanded.value = false
            }
            is CategoryEditEvent.OnTermChange -> {
                newWordTerm.value = event.term
            }
            is CategoryEditEvent.OnDefinitionChange -> {
                newWordDefinition.value = event.definition
            }
            is CategoryEditEvent.OnDeleteWord -> {
                viewModelScope.launch {
                    wordRepository.delete(event.id)
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

                viewModelScope.launch {
                    var definition = ""
                    if (!newWordDefinition.value.isEmpty()) {
                        definition = newWordDefinition.value
                    } else {
                        for (translation in wordTranslations) {
                            definition += "$translation, "
                        }
                        definition = definition.dropLast(2)
                    }

                    wordRepository.create(Word(
                        id = 0,
                        term = newWordTerm.value,
                        definition = definition,
                        category = category.id,
                        created = Date(),
                        lastRepeated = Date(),
                    ))
                }

                newWordTerm.value = ""
                newWordDefinition.value = ""
                openDialog.value = false
                _state.value = WordTranslationState(translation = null)
                wordTranslations.clear()
            }
            is CategoryEditEvent.OnOpenAddWordDialog -> {
                openDialog.value = true
            }
            is CategoryEditEvent.OnCloseAddWordDialog -> {
                newWordTerm.value = ""
                newWordDefinition.value = ""
                openDialog.value = false
                _state.value = WordTranslationState(translation = null)
                wordTranslations.clear()
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
                                    println("APIYANDEX: loaded")
                                }
                                is Resource.Error -> {
                                    _state.value = state.value.copy(
                                        isLoading = false
                                    )
                                    println("APIYANDEX: " + (result.message ?: "Unknown error"))
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
        }
    }
}