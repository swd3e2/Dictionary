package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateOf
import com.dictionary.domain.entity.Word

class WriteState {
    var currentWord = mutableStateOf<Word?>(null)
        private set

    var words = mutableListOf<Word>()
        private set

    var definition = mutableStateOf("")
    var hasError = mutableStateOf(false)
        private set

    var hasDefinition = mutableStateOf("")
        private set
    var wantDefinition = mutableStateOf("")
        private set

    var index = 0
    var lastAddedWordId = 0
}