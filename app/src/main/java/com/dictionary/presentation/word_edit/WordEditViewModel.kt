package com.dictionary.presentation.word_edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.common.Resource
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.TranslationRepository
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.category_edit.WordTranslationState
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordEditViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val translationRepository: TranslationRepository,
    savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var menuExpanded = mutableStateOf(false)
        private set

    var showTranslationDialog = mutableStateOf(false)
        private set

    var showWordDeleteDialog = mutableStateOf(false)
        private set

    var selectedTranslations = mutableStateListOf<String>()
        private set

    var word: Word? = null
        private set

    var newTerm = mutableStateOf("")
        private set

    var newDefinition = mutableStateOf("")
        private set

    var newSynonyms = mutableStateOf("")
        private set

    var newAntonyms = mutableStateOf("")
        private set

    var newTranscription = mutableStateOf("")
        private set

    var newSimilar = mutableStateOf("")
        private set

    var editState = mutableStateOf(true)
        private set

    var wordWithTermExistsInCategory = mutableStateOf<String?>(null)
        private set

    private val _state = mutableStateOf(WordTranslationState(translation = null, isLoading = false))
    val state: State<WordTranslationState> = _state

    private var searchJob: Job? = null

    var categoryId: Int? = null

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if(id != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                wordRepository.get(id)?.let { w ->
                    withContext(Dispatchers.Main) {
                        word = w
                        newTerm.value = w.term
                        newDefinition.value = w.definition
                        newSynonyms.value = w.synonyms
                        newAntonyms.value = w.antonyms
                        newTranscription.value = w.transcription
                        newSimilar.value = w.similar
                        editState.value = false
                    }
                }
            }
        }
        categoryId = savedStateHandle.get<Int>("category")!!
    }

    fun onEvent(event: WordEditEvent) {
        when (event) {
            is WordEditEvent.OnMenuClick -> {
                menuExpanded.value = true
            }
            is WordEditEvent.OnCloseMenu -> {
                menuExpanded.value = false
            }
            is WordEditEvent.OnTermChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    wordWithTermExistsInCategory.value = wordRepository.category(event.term)
                }
                newTerm.value = event.term
            }
            is WordEditEvent.OnDefinitionChange -> {
                newDefinition.value = event.definition
            }
            is WordEditEvent.OnSynonymsChange -> {
                newSynonyms.value = event.synonyms
            }
            is WordEditEvent.OnAntonymsChange -> {
                newAntonyms.value = event.antonyms
            }
            is WordEditEvent.OnSimilarChange -> {
                newSimilar.value = event.similar
            }
            is WordEditEvent.OnTranscriptionChange -> {
                newTranscription.value = event.transcription
            }
            is WordEditEvent.OnSaveClick -> {
                if (newTerm.value.isEmpty()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                "Term is empty"
                            )
                        )
                    }
                    return
                }
                if (newDefinition.value.isEmpty()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                "Definition is empty"
                            )
                        )
                    }
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    if (word != null) {
                        wordRepository.save(word!!.apply {
                            term = newTerm.value
                            definition =  newDefinition.value
                            synonyms = newSynonyms.value
                            antonyms = newAntonyms.value
                            transcription = newTranscription.value
                            similar =  newSimilar.value
                        })
                        _eventFlow.emit(UiEvent.ShowSnackbar("Saved"))
                        editState.value = false
                    } else {
                        wordRepository.save(Word(
                            term = newTerm.value,
                            category = categoryId,
                            definition =  newDefinition.value,
                            synonyms = newSynonyms.value,
                            antonyms = newAntonyms.value,
                            transcription = newTranscription.value,
                            similar =  newSimilar.value,
                        ))
                        _eventFlow.emit(UiEvent.PopBackStack)
                    }
                }
            }
            WordEditEvent.OnShowTranslationDialog -> {
                if (newTerm.value.isEmpty()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Term is empty"))
                    }
                    return
                }

                showTranslationDialog.value = true
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    translationRepository.getTranslation(newTerm.value)
                        .onEach { result ->
                            when (result) {
                                is Resource.Success -> {
                                    _state.value = state.value.copy(translation = result.data, isLoading = false)
                                }
                                is Resource.Error -> {
                                    _state.value = state.value.copy(isLoading = false)
                                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unexpected error occurred"))
                                }
                                is Resource.Loading -> {
                                    _state.value = state.value.copy(isLoading = true)
                                }
                            }
                        }.launchIn(this)
                }
            }
            is WordEditEvent.OnClickOnTranslation -> {
                if (!selectedTranslations.contains(event.translation)) {
                    selectedTranslations.add(event.translation)
                } else {
                    selectedTranslations.remove(event.translation)
                }
            }
            WordEditEvent.OnApplyTranslation -> {
                if (selectedTranslations.isEmpty()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("No definitions selected"))
                    }
                    return
                }
                newDefinition.value = selectedTranslations.reduce { acc, s -> "$acc, $s" }
                selectedTranslations.clear()
                showTranslationDialog.value = false
            }
            WordEditEvent.OnHideTranslationDialog -> {
                searchJob?.cancel()
                showTranslationDialog.value = false
            }
            WordEditEvent.GoBack -> viewModelScope.launch {
                _eventFlow.emit(UiEvent.PopBackStack)
            }
            WordEditEvent.OnEdit -> editState.value = true
            WordEditEvent.OnView -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ClearFocus)
                }
                editState.value = false
            }
            WordEditEvent.OnDeleteWord ->  viewModelScope.launch(Dispatchers.IO) {
                wordRepository.delete(word!!.id)
                _eventFlow.emit(UiEvent.PopBackStack)
            }
            WordEditEvent.OnHideWordDeleteDialog -> {
                showWordDeleteDialog.value = false
            }
            WordEditEvent.OnShowWordDeleteDialog -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ClearFocus)
                }
                showWordDeleteDialog.value = true
            }
        }
    }
}