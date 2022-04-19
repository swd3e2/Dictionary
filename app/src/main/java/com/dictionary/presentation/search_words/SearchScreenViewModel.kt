package com.dictionary.presentation.search_words

import androidx.lifecycle.ViewModel
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.WordRepository
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    categoryRepository: CategoryRepository,
    wordRepository: WordRepository,
): ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
}