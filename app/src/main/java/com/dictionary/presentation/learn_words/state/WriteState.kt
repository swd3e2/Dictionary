package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateOf
import com.dictionary.domain.entity.Word

class WriteState {
    var currentWord = mutableStateOf<Word?>(null)
    var words = mutableListOf<Word>()
    var definition = mutableStateOf("")
    var wantDefinition = mutableStateOf("")
    var index = 0
    var lastAddedWordId = 0
}