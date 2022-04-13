package com.dictionary.presentation.learn_words.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.dictionary.presentation.models.LearnWord

class MatchState {
    var currentWordsGroupIndex: Int = 0
    var currentWordsGroup: SnapshotStateList<LearnWord> = mutableStateListOf()
    var wordsGroups: MutableList<MutableList<LearnWord>> = mutableListOf()

    var successCount: Int = 0
    var wordsState: SnapshotStateMap<Int, String> = mutableStateMapOf()
    var wordsSelected: MutableList<LearnWord> = mutableListOf()
}