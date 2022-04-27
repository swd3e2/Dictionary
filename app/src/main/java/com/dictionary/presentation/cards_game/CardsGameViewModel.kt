package com.dictionary.presentation.cards_game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CardsGameViewModel @Inject constructor(
    private val wordsRepository: WordRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var currentWord = mutableStateOf<Word?>(null)
        private set

    var currentWordIndex = mutableStateOf(1)
        private set

    var countOfWords = mutableStateOf(0)
        private set

    var learnProgress = mutableStateOf(0f)
        private set

    var learnedWordsCount = mutableStateOf(0)
        private set

    var notLearnedWordsCount = mutableStateOf(0)
        private set

    var isLoading = mutableStateOf(true)
        private set

    var wordsToLearn = mutableStateListOf<Word>()
    private var forgottenWords = mutableSetOf<Int>()

    init {
        val id = savedStateHandle.get<Int>("id")!!
        viewModelScope.launch(Dispatchers.IO) {

            val words = (if (id != -1)
                wordsRepository.listByCategoryToRepeat(id)
            else wordsRepository.listToRepeat())

            withContext(Dispatchers.Main) {
                wordsToLearn.addAll(words)

                if (!wordsToLearn.isEmpty()) {
                    wordsToLearn.shuffle()
                    currentWord.value = wordsToLearn.first()
                    currentWordIndex.value = 0
                    countOfWords.value = wordsToLearn.size
                }
                isLoading.value = false
            }
        }
    }

    fun onEvent(event: CardsGameEvent) {
        when (event) {
            is CardsGameEvent.WordLearned -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (!forgottenWords.contains(event.word.id)) {
                        wordsRepository.update(event.word.apply {
                            lastRepeated = Date()
                            bucket++
                        })
                    }

                    updateNextWord()
                    updateProgress(true)
                }
            }
            is CardsGameEvent.WordNotLearned -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (!forgottenWords.contains(event.word.id)) {
                        forgottenWords.add(event.word.id)
                        wordsToLearn.add(event.word)
                    }

                    wordsRepository.update(event.word.apply {
                        lastRepeated = Date()
                        bucket = 1
                    })

                    updateNextWord()
                    updateProgress(false)
                }
            }
        }
    }

    private fun updateNextWord() {
        wordsToLearn.remove(currentWord.value)
        currentWord.value = if (!wordsToLearn.isEmpty()) wordsToLearn.first() else null
        currentWordIndex.value = currentWordIndex.value + 1
    }

    private fun updateProgress(learned: Boolean) {
        if (learned) {
            learnProgress.value = currentWordIndex.value.toFloat() / countOfWords.value.toFloat()
            learnedWordsCount.value += 1
        } else {
            notLearnedWordsCount.value += 1
        }
    }
}