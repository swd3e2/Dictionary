package com.dictionary.presentation.word_edit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordEditViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var menuExpanded = mutableStateOf(false)
        private set

    var word: Word? = null
        private set

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if(id != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                wordRepository.get(id)?.let { w -> word = w }
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
        }
    }
}