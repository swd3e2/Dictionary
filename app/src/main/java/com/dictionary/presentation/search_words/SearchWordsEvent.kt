package com.dictionary.presentation.search_words

import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.category_edit.CategoryEditEvent

sealed class SearchWordsEvent {
    data class OnSearchTermChange(val term: String) : SearchWordsEvent()
    object OnShowSortDialog: SearchWordsEvent()
    object OnHideSortDialog: SearchWordsEvent()
    data class OnSortChange(var field: String, var direction: String) : SearchWordsEvent()
    data class OnDropWordBucket(val word: Word) : SearchWordsEvent()

    data class OnShowMoveWordToCategoryDialog(val word: Word) : SearchWordsEvent()
    object OnHideMoveWordToCategoryDialog : SearchWordsEvent()
    data class OnMoveWordToCategory(val category: Category): SearchWordsEvent()

    data class OnShowWordDeleteDialog(val word: Word) : SearchWordsEvent()
    data class OnWordClick(val id: Int) : SearchWordsEvent()

    object OnHideWordDeleteDialog : SearchWordsEvent()
    object OnDeleteWord: SearchWordsEvent()
}