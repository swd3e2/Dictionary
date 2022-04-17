package com.dictionary.presentation.test_game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.learn_words.state.TestState
import com.dictionary.utils.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TestGameViewModel @Inject constructor(
    private val wordsRepository: WordRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var testState = TestState()

    init {
        val id = savedStateHandle.get<Int>("id")!!
        viewModelScope.launch(Dispatchers.IO) {
            val words = (if (id != -1)
                wordsRepository.categoryWordsAsList(id)
            else wordsRepository.asList()).filter { it.shouldBeRepeated() }
            if (words.isEmpty()) {
                return@launch
            }

            withContext(Dispatchers.Main) {
                testState.init(words)
            }
        }
    }
}