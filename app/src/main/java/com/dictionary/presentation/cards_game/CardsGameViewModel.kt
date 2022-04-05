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
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CardsGameViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val wordsRepository: WordRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var category: Category = Category(0, "")
        private set

    var currentWord = mutableStateOf<Word?>(null)
        private set

    private var words: List<Word> = emptyList()
    private var wordsToLearn = mutableStateListOf<Word>()

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if(id != -1) {
            viewModelScope.launch {
                categoryRepository.get(id)?.let { c ->
                    category = c
                    words = wordsRepository.categoryWordsAsList(c.id!!)

                    for (word in words) {
                        if (word.shouldBeLearned()) {
                            wordsToLearn.add(word)
                        }
                    }

                    if (!wordsToLearn.isEmpty()) {
                        currentWord.value = wordsToLearn.first()
                    }
                }
            }
        }
    }

    fun onEvent(event: CardsGameEvent) {
        when (event) {
            is CardsGameEvent.WordLearned -> {
                val word = event.word
                word.lastRepeated = Date()
                word.bucket = word.bucket+1
                wordsRepository.update(word)

                wordsToLearn.remove(currentWord.value)
                currentWord.value = if (!wordsToLearn.isEmpty()) wordsToLearn.first() else null
            }
            is CardsGameEvent.WordNotLearned -> {
                val word = event.word
                word.lastRepeated = Date()
                word.bucket = 0
                wordsRepository.update(word)

                wordsToLearn.remove(currentWord.value)
                currentWord.value = if (!wordsToLearn.isEmpty()) wordsToLearn.first() else null
            }
        }
    }
}