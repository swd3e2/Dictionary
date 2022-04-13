package com.dictionary.presentation.cards_game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.WordRepository
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

    private var words: List<Word> = emptyList()
    private var wordsToLearn = mutableStateListOf<Word>()

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if (id != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                words = wordsRepository.categoryWordsAsList(id)
                withContext(Dispatchers.Main) {
                    for (word in words) {
                        if (word.bucket == 0) {
                            wordsToLearn.add(word)
                        }
                    }

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
    }

    fun onEvent(event: CardsGameEvent) {
        when (event) {
            is CardsGameEvent.WordLearned -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val word = event.word
                    word.lastRepeated = Date()
                    word.bucket = word.bucket+1
                    wordsRepository.update(word)

                    wordsToLearn.remove(currentWord.value)
                    currentWord.value = if (!wordsToLearn.isEmpty()) wordsToLearn.first() else null
                    currentWordIndex.value = currentWordIndex.value + 1
                    learnProgress.value =
                        currentWordIndex.value.toFloat() / countOfWords.value.toFloat()
                    learnedWordsCount.value = learnedWordsCount.value + 1
                }
            }
            is CardsGameEvent.WordNotLearned -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val word = event.word
                    word.lastRepeated = Date()
                    word.bucket = 0
                    wordsRepository.update(word)

                    wordsToLearn.remove(currentWord.value)
                    currentWord.value = if (!wordsToLearn.isEmpty()) wordsToLearn.first() else null
                    currentWordIndex.value = currentWordIndex.value + 1
                    learnProgress.value =
                        currentWordIndex.value.toFloat() / countOfWords.value.toFloat()
                    notLearnedWordsCount.value = notLearnedWordsCount.value + 1
                }
            }
        }
    }
}