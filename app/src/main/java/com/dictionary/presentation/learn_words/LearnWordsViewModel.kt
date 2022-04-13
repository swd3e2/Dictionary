package com.dictionary.presentation.learn_words

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.models.LearnWord
import com.dictionary.presentation.learn_words.state.CardsState
import com.dictionary.presentation.learn_words.state.MatchState
import com.dictionary.presentation.learn_words.state.TestState
import com.dictionary.presentation.learn_words.state.WriteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LearnWordsViewModel @Inject constructor(
    private val wordsRepository: WordRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    val wordsToLearn = mutableListOf<Word>()
    val currentStep = mutableStateOf(1)
    var currentWords = mutableStateListOf<Word>()

    var isLoading = mutableStateOf(true)
        private set

    val matchState = MatchState()
    val testState = TestState()
    val cardsState = CardsState()
    val writeState = WriteState()

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if (id != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                val words = wordsRepository.categoryWordsAsList(id)
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
    }

    fun onEvent(event: LearnWordsEvent) {
        when (event) {
            is LearnWordsEvent.OnGoToMatch -> {
                currentStep.value = 2
                val listSize = currentWords.size

                var group = mutableListOf<LearnWord>()
                for ((index, word) in currentWords.withIndex()) {
                    if (group.size == 12) {
                        group.shuffle()
                        matchState.wordsGroups.add(group)
                        group = mutableListOf()
                    }
                    group.add(LearnWord(text = word.term, wordId = word.id, index = index))
                    group.add(LearnWord(text = word.definition, wordId = word.id, index = index + listSize))

                    matchState.wordsState[index] = "unselected"
                    matchState.wordsState[index + listSize] = "unselected"
                }

                if (group.size > 0) {
                    group.shuffle()
                    matchState.wordsGroups.add(group)
                }
                matchState.currentWordsGroupIndex = 0
                matchState.currentWordsGroup.addAll(matchState.wordsGroups[matchState.currentWordsGroupIndex])
            }
            is LearnWordsEvent.OnMatchSelect -> {
                if (matchState.wordsState[event.word.index] == "selected") {
                    matchState.wordsState[event.word.index] = "unselected"
                    matchState.wordsSelected.remove(event.word)
                    return
                }

                matchState.wordsState[event.word.index] = "selected"
                matchState.wordsSelected.add(event.word)
                if (matchState.wordsSelected.size == 2) {
                    val first = matchState.wordsSelected[0]
                    val second = matchState.wordsSelected[1]
                    matchState.wordsSelected.clear()

                    val isOK = first.wordId == second.wordId

                    if (isOK) {
                        matchState.wordsState[first.index] = "success"
                        matchState.wordsState[second.index] = "success"
                        matchState.successCount += 2

                        if (matchState.successCount == matchState.currentWordsGroup.size) {
                            viewModelScope.launch {
                                delay(300)
                                matchState.successCount = 0

                                matchState.currentWordsGroupIndex++
                                if (matchState.wordsGroups.size > matchState.currentWordsGroupIndex) {
                                    matchState.currentWordsGroup.clear()
                                    matchState.currentWordsGroup.addAll(matchState.wordsGroups[matchState.currentWordsGroupIndex])
                                } else {
                                    onEvent(LearnWordsEvent.OnGoToTest)
                                }
                            }
                        }
                    } else {
                        matchState.wordsState[first.index] = "error"
                        matchState.wordsState[second.index] = "error"
                        viewModelScope.launch {
                            delay(500)
                            matchState.wordsState[first.index] = "unselected"
                            matchState.wordsState[second.index] = "unselected"
                        }
                    }
                }
            }
            is LearnWordsEvent.OnGoToTest -> {
                currentStep.value = 3
                testState.addAll(currentWords)
                testState.currentWord.value = testState.words[testState.index]
            }
            is LearnWordsEvent.OnTestSelect -> {
                if (testState.currentWord.value!!.word.id == event.wordId) {
                    testState.wordsState[event.index] = "success"
                    testState.index++
                    if (testState.index >= testState.words.size) {
                        onEvent(LearnWordsEvent.OnGoToCards)
                        return
                    }
                    viewModelScope.launch {
                        delay(300)
                        testState.currentWord.value = testState.words[testState.index]
                    }
                } else {
                    testState.wordsState[event.index] = "error"
                    viewModelScope.launch {
                        delay(400)
                        testState.wordsState[event.index] = "unselected"
                    }
                    testState.addWord(event.word, currentWords)
                }
            }
            is LearnWordsEvent.OnGoToCards -> {
                currentStep.value = 4
                cardsState.addAll(currentWords)
                cardsState.currentWord.value = cardsState.words[cardsState.index]
            }
            is LearnWordsEvent.OnCardLeftSwipe -> {
                cardsState.words.add(cardsState.words[cardsState.index])
                cardsState.index++
                cardsState.currentWord.value = cardsState.words[cardsState.index]
            }
            is LearnWordsEvent.OnCardRightSwipe -> {
                cardsState.index++
                if (cardsState.index >= cardsState.words.size) {
                    onEvent(LearnWordsEvent.OnGoToWrite)
                    return
                }
                cardsState.currentWord.value = cardsState.words[cardsState.index]
            }
            is LearnWordsEvent.OnGoToWrite -> {
                currentStep.value = 5
                writeState.words.addAll(currentWords)
                writeState.currentWord.value = writeState.words[writeState.index]
            }
            is LearnWordsEvent.OnWriteTryDefinition -> {
                val word = writeState.currentWord.value!!
                val possibleDefinitions = word.definition.split(',')

                var guessedRight = false
                for (possibleDefinition in possibleDefinitions) {
                    guessedRight = guessedRight || possibleDefinition.trim().lowercase() == writeState.definition.value.lowercase()
                }

                if (guessedRight) {
                    writeState.hasError.value = false
                    writeState.index++
                    if (writeState.index >= writeState.words.size) {
                        onEvent(LearnWordsEvent.OnGoToDone)
                        return
                    }
                    writeState.currentWord.value = writeState.words[writeState.index]
                    writeState.definition.value = ""
                    return
                } else {
                    writeState.hasError.value = true
                    if (writeState.lastAddedWordId != word.id) {
                        writeState.lastAddedWordId = word.id
                        writeState.words.add(writeState.words[writeState.index])
                    }

                    writeState.hasDefinition.value = writeState.definition.value
                    writeState.wantDefinition.value = word.definition
                    writeState.definition.value = ""
                }
            }
            is LearnWordsEvent.OnGoToDone -> {
                currentStep.value = 6
            }
            is LearnWordsEvent.OnStartNew -> {
                currentWords.clear()
                val currentWordsToLearn = wordsToLearn.take(18)
                wordsToLearn.removeAll(currentWordsToLearn)
                currentWords.addAll(currentWordsToLearn)
                currentStep.value = 1
            }
        }
    }
}