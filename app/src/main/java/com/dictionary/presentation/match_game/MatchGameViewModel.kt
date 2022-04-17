package com.dictionary.presentation.match_game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.learn_words.state.MatchState
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MatchGameViewModel @Inject constructor(
    private val wordsRepository: WordRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var matchState = MatchState()

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
                matchState.init(words)
            }
        }
    }

    fun onEvent(event: MatchGameEvent) {
        when (event) {
            is MatchGameEvent.OnMatchSelect -> {
                val state = matchState.onWordSelect(event.word)
                val word = event.word
                when (state) {
                    is MatchState.State.WordSelected -> {
                        matchState.setSelectedState(word.index)
                    }
                    is MatchState.State.WordDeselected -> {
                        matchState.setDeselectedState(word.index)
                    }
                    is MatchState.State.MatchWrong -> {
                        val first = state.first
                        val second = state.second

                        matchState.setErrorState(first.index, second.index)

                        viewModelScope.launch {
                            delay(500)
                            matchState.setDeselectedState(first.index, second.index)
                        }
                    }
                    is MatchState.State.MatchRight -> {
                        val first = state.first
                        val second = state.second

                        matchState.setSuccessState(first.index, second.index)
                        if (matchState.canGoNextGroup()) {
                            viewModelScope.launch {
                                delay(300)
                                matchState.resetSuccessCount()

                                if (!matchState.selectNextGroup()) {
                                    _eventFlow.emit(UiEvent.PopBackStack)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}