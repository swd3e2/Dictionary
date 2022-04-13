package com.dictionary.presentation.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dictionary.domain.entity.Word

data class WordsWithSuggest(
    val word: Word,
    val suggested: SnapshotStateList<WordWithIndex>
)