package com.dictionary.presentation.category_list

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Category
import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.WordRepository
import com.dictionary.utils.Routes
import com.dictionary.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.util.*
import javax.inject.Inject


@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val app: Application,
    private val categoryRepository: CategoryRepository,
    private val wordsRepository: WordRepository
) : AndroidViewModel(app) {

    var search = mutableStateOf("")
        private set

    var showAddCategoryDialog = mutableStateOf(false)
        private set

    var title = mutableStateOf("")
        private set

    val categories = categoryRepository.listWithWords()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.OnSaveClick -> {
                if (title.value.isEmpty()) {
                    return
                }
                viewModelScope.launch(Dispatchers.IO) {
                    val newCategory = Category(id = 0, name = title.value)
                    val newCategoryId = categoryRepository.create(newCategory)
                    newCategory.id = newCategoryId.toInt()
                    title.value = ""
                    showAddCategoryDialog.value = false
                }
            }
            is CategoryListEvent.OnChangeTitle -> {
                title.value = event.title
            }
            is CategoryListEvent.OnCategoryClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.CATEGORY_EDIT + "?id=${event.id}"))
            }
            is CategoryListEvent.OnShowAddCategoryDialog -> {
                showAddCategoryDialog.value = true
            }
            is CategoryListEvent.OnHideAddCategoryDialog -> {
                title.value = ""
                showAddCategoryDialog.value = false
            }
            is CategoryListEvent.OnImportFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val readBytes = app.contentResolver.openInputStream(event.uri)!!.readBytes()
                    val data = readBytes.toString(Charsets.UTF_8)

                    var newCategoryId = 0L
                    val words = mutableListOf<Word>()
                    for (line in data.lines()) {
                        val splitLine = line.split(';')
                        if (splitLine[0] == "category") {
                            newCategoryId = categoryRepository.create(Category(id = 0, name = splitLine[1]))
                            continue
                        }
                        val word = Word(category = newCategoryId.toInt())
                        for ((index, splitRow) in splitLine.withIndex()) {
                            when (index) {
                                0 -> word.term = splitRow
                                1 -> word.definition = splitRow
                                2 -> {
                                    if (splitRow.isNotEmpty()) {
                                        word.created = Date(splitRow.toLong())
                                    }
                                }
                                3 -> {
                                    if (splitRow.isNotEmpty()) {
                                        word.lastRepeated = Date(splitRow.toLong())
                                    }
                                }
                                4 -> {
                                    if (splitRow.isNotEmpty()) {
                                        word.firstLearned = Date(splitRow.toLong())
                                    }
                                }
                                5 -> word.bucket = splitRow.toInt()
                                6 -> word.synonyms = splitRow
                                7 -> word.antonyms = splitRow
                                8 -> word.similar = splitRow
                                9 -> word.transcription = splitRow
                            }
                        }
                        words.add(word)
                    }

                    if (words.size == 0) {
                        cancel()
                    }

                    wordsRepository.batchCreate(words)
                }
            }
            is CategoryListEvent.OnExportFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val categories = categoryRepository.listWithWordsAsList()
                    if (categories.isEmpty()) {
                        sendUiEvent(UiEvent.ShowSnackbar("No data to export"))
                        return@launch
                    }

                    val file = File(Environment.getExternalStorageDirectory().path + "/Download/export_data.csv")

                    if (!file.exists() && !file.createNewFile()) {
                        sendUiEvent(UiEvent.ShowSnackbar("Cant create file"))
                        return@launch
                    }

                    FileOutputStream(file).use { output ->
                        for ((categoryIndex, category) in categories.withIndex()) {
                            output.write("category;${category.category.name}\n".toByteArray())
                            for ((wordIndex, word) in category.words.withIndex()) {
                                output.write(
                                    (
                                        "${word.term};" +
                                        "${word.definition};" +
                                        "${word.created.toInstant().epochSecond};" +
                                        "${if (word.lastRepeated != null) word.lastRepeated!!.toInstant().epochSecond else ""};" +
                                        "${if (word.firstLearned != null) word.firstLearned!!.toInstant().epochSecond else ""};" +
                                        "${word.bucket};" +
                                        "${word.synonyms};" +
                                        "${word.antonyms};" +
                                        "${word.similar};" +
                                        "${word.transcription}" +
                                        if (categoryIndex == categories.size - 1 && wordIndex == category.words.size - 1) "" else "\n"
                                    ).toByteArray()
                                )
                            }
                        }
                    }
                    sendUiEvent(UiEvent.ShowSnackbar("All data exported"))
                }
            }
            is CategoryListEvent.OnSearchChange -> {
                search.value = event.search
            }
            is CategoryListEvent.OnGoToCardsGame -> {
                sendUiEvent(UiEvent.Navigate(Routes.CARDS_GAME))
            }
            is CategoryListEvent.OnGoToLearnWords -> {
                sendUiEvent(UiEvent.Navigate(Routes.LEARN_WORDS))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun getFilename(uri: Uri): String {
        val path = uri.path
        val splitPath = path?.split('/')
        if (splitPath == null || splitPath.isEmpty()) {
            return ""
        }

        val splitFileExtension = splitPath[splitPath.size - 1].split('.')
        if (splitFileExtension.size < 2) {
            return ""
        }

        return splitFileExtension[0]
    }
}