package com.dictionary.presentation.learn_words

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.learn_words.state.CardsState
import com.dictionary.presentation.learn_words.state.MatchState
import com.dictionary.presentation.learn_words.state.TestState
import com.dictionary.presentation.learn_words.state.WriteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LearnWordsViewModel @Inject constructor(
    private val wordsRepository: WordRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val wordsToLearn = mutableListOf<Word>()
    val currentStep = mutableStateOf(1)
    var currentWords = mutableStateListOf<Word>()

    var isLoading = mutableStateOf(true)
        private set

    var learnProgress = mutableStateOf(0f)
        private set

    val matchState = MatchState()
    val testState = TestState()
    val cardsState = CardsState()
    val writeState = WriteState()

    var lastWordWrong = false

    init {
        val id = savedStateHandle.get<Int>("id")!!
        viewModelScope.launch(Dispatchers.IO) {
            val words = (if (id != -1)
                wordsRepository.categoryWordsAsList(id)
            else wordsRepository.asList()).filter { it.bucket == 0 }

            withContext(Dispatchers.Main) {
                wordsToLearn.addAll(words)
                if (wordsToLearn.isNotEmpty()) {
                    wordsToLearn.shuffle()
                    val currentWordsToLearn = wordsToLearn.take(18)
                    wordsToLearn.removeAll(currentWordsToLearn)
                    currentWords.addAll(currentWordsToLearn)
                }
                isLoading.value = false
            }
        }
    }

    fun onEvent(event: LearnWordsEvent) {
        when (event) {
            is LearnWordsEvent.OnGoToMatch -> {
                currentStep.value = 2
                matchState.init(currentWords)
            }
            is LearnWordsEvent.OnMatchSelect -> {
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
                        addProgress()
                        val first = state.first
                        val second = state.second

                        matchState.setSuccessState(first.index, second.index)

                        viewModelScope.launch {
                            delay(100)
                            matchState.setHideState(first.index, second.index)
                            if (matchState.canGoNextGroup()) {
                                matchState.resetSuccessCount()

                                if (!matchState.selectNextGroup()) {
                                    onEvent(LearnWordsEvent.OnGoToTest)
                                }
                            }
                        }
                    }
                }
            }
            is LearnWordsEvent.OnGoToTest -> {
                currentStep.value = 3
                testState.init(currentWords)
            }
            is LearnWordsEvent.OnTestSelect -> {
                when (testState.onSelect(event.selected)) {
                    is TestState.State.SelectRight -> {
                        testState.setSuccessState(event.selected.index)
                        viewModelScope.launch {
                            delay(200)
                            testState.selectNext()
                        }
                        addProgress()
                    }
                    is TestState.State.SelectWrong -> {
                        testState.setErrorState(event.selected.index)
                        viewModelScope.launch {
                            delay(200)
                            testState.setDeselectedState(event.selected.index)
                        }
                        lastWordWrong = true
                    }
                    is TestState.State.GameEnd -> {
                        addProgress()
                        onEvent(LearnWordsEvent.OnGoToCards)
                        return
                    }
                }
            }
            is LearnWordsEvent.OnGoToCards -> {
                currentStep.value = 4
                cardsState.init(currentWords)
            }
            is LearnWordsEvent.OnCardLeftSwipe -> {
                cardsState.doesNotKnowWord()
                cardsState.selectNext()
            }
            is LearnWordsEvent.OnCardRightSwipe -> {
                addProgress()
                if (cardsState.noMoreWords()) {
                    onEvent(LearnWordsEvent.OnGoToWrite)
                    return
                }
                cardsState.selectNext()
            }
            is LearnWordsEvent.OnGoToWrite -> {
                currentStep.value = 5
                writeState.init(currentWords)
            }
            is LearnWordsEvent.OnWriteTryDefinition -> {
                val guessedRight = writeState.tryGuess()
                val word = writeState.currentWord.value!!.copy()

                if (guessedRight) {
                    viewModelScope.launch(Dispatchers.IO) {
                        wordsRepository.create(word.apply {
                            bucket = 1
                            firstLearned = Date()
                            lastRepeated = Date()
                        })
                    }
                    addProgress()
                } else {
                    lastWordWrong = true
                }

                if (guessedRight && !writeState.selectNext()) {
                    onEvent(LearnWordsEvent.OnGoToDone)
                    return
                }
            }
            is LearnWordsEvent.OnGoToDone -> {
                currentStep.value = 6
            }
            is LearnWordsEvent.OnStartNew -> {
                wordsToLearn.removeAll(currentWords)
                currentWords.clear()
                val currentWordsToLearn = wordsToLearn.take(18)
                learnProgress.value = 0f
                currentWords.addAll(currentWordsToLearn)
                currentStep.value = 1
            }
        }
    }

    private fun addProgress() {
        if (lastWordWrong) {
            lastWordWrong = false
            return
        }
        learnProgress.value += 100f / currentWords.count() / 4f / 100f
    }
}