package com.dictionary.presentation.learn_words

import androidx.compose.runtime.mutableStateListOf
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
class LearnWordsViewModel @Inject constructor(
    wordsRepository: WordRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {


    val wordsToLearn = mutableListOf<Word>()
    val currentWords = mutableStateListOf<Word>()

    var isLoading = mutableStateOf(true)
        private set

    var askContinueOrRenew = mutableStateOf(true)
        private set


    init {
        val id = savedStateHandle.get<Int>("id")!!
        if (id != -1) {
            val items : MutableList<Word>? = savedStateHandle["asd"]
            if (items != null) {

            }


            viewModelScope.launch(Dispatchers.IO) {
                wordsToLearn.addAll(wordsRepository.categoryWordsAsList(id))

                if (wordsToLearn.isNotEmpty()) {
                    wordsToLearn.shuffle()
                    currentWords.addAll(wordsToLearn.take(20))
                }
                isLoading.value = false
            }
        }
    }

}