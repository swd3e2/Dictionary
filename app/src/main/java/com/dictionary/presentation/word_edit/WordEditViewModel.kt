package com.dictionary.presentation.word_edit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.category_edit.CategoryEditViewModel
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordEditViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var menuExpanded = mutableStateOf(false)
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
                    }
                }
            }
        }
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
                viewModelScope.launch(Dispatchers.IO) {
                    wordRepository.create(word!!.apply {
                        term = newTerm.value
                        definition =  newDefinition.value
                        synonyms = newSynonyms.value
                        antonyms = newAntonyms.value
                        transcription = newTranscription.value
                        similar =  newSimilar.value
                    })
                    _eventFlow.emit(UiEvent.ShowSnackbar(
                        "Saved"
                    ))
                    _eventFlow.emit(UiEvent.PopBackStack)
                }
            }
        }
    }
}