package com.dictionary.presentation.category_list

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val app: Application,
    private val categoryRepository: CategoryRepository,
    private val wordsRepository: WordRepository
) : AndroidViewModel(app) {

    var isLoading = mutableStateOf(false)
        private set

    var showAddCategoryDialog = mutableStateOf(false)
        private set

    var showDeleteCategoryDialog = mutableStateOf(false)
        private set

    var title = mutableStateOf("")
        private set

    val countByCategory = mutableStateMapOf<Int, Int>()
    val countToLearn = mutableStateMapOf<Int, Int>()
    val countToRepeat = mutableStateMapOf<Int, Int>()

    var categories = mutableStateListOf<Category>()
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var selectedCategory: Category? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.flowList().collect{
                withContext(Dispatchers.Main) {
                    categories.clear()
                    categories.addAll(it)
                }
            }
        }
    }

    fun loadCount() {
        countToRepeat.clear()
        countToLearn.clear()
        countByCategory.clear()
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.countGrouped().forEach{ pair ->
                withContext(Dispatchers.Main) {
                    countByCategory[pair.first] = pair.second
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.countToLearnGrouped().forEach{ pair ->
                withContext(Dispatchers.Main) {
                    countToLearn[pair.first] = pair.second
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.countToRepeatGrouped().forEach{ pair ->
                withContext(Dispatchers.Main) {
                    countToRepeat[pair.first] = pair.second
                }
            }
        }
    }

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
                importData(event)
            }
            is CategoryListEvent.OnExportFile -> {
                exportData()
            }
            is CategoryListEvent.OnGoToCardsGame -> {
                sendUiEvent(UiEvent.Navigate(Routes.CARDS_GAME))
            }
            is CategoryListEvent.OnGoToLearnWords -> {
                sendUiEvent(UiEvent.Navigate(Routes.LEARN_WORDS))
            }
            CategoryListEvent.OnDeleteCategory -> {
                selectedCategory?.let { category ->
                    viewModelScope.launch(Dispatchers.IO) {
                        categoryRepository.delete(category.id)
                        wordsRepository.deleteByCategory(category.id)
                        if (category.image.isNotEmpty()) {
                            try {
                                val currentFile = File(category.image)
                                currentFile.exists() && currentFile.delete()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        showDeleteCategoryDialog.value = false
                        _uiEvent.send(UiEvent.ShowSnackbar("Category ${category.name} deleted"))
                    }
                }
            }
            CategoryListEvent.OnHideDeleteCategoryDialog -> showDeleteCategoryDialog.value = false
            is CategoryListEvent.OnShowDeleteCategoryDialog -> {
                showDeleteCategoryDialog.value = true
                selectedCategory = event.category
            }
        }
    }

    private fun importData(event: CategoryListEvent.OnImportFile) {
        viewModelScope.launch(Dispatchers.IO) {
            val readBytes = app.contentResolver.openInputStream(event.uri)!!.readBytes()
            val data = readBytes.toString(Charsets.UTF_8)

            var newCategoryId = 0L
            val words = mutableListOf<Word>()
            for (line in data.lines()) {
                val splitLine = line.split(';')
                if (splitLine[0] == "category") {
                    newCategoryId =
                        categoryRepository.create(Category(id = 0, name = splitLine[1]))
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

            wordsRepository.batchSave(words)
            loadCount()
        }
    }

    private fun exportData() {
        viewModelScope.launch(Dispatchers.IO) {
            val categories = categoryRepository.listWithWords()
            if (categories.isEmpty()) {
                sendUiEvent(UiEvent.ShowSnackbar("No data to export"))
                return@launch
            }

            val dateFormat = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm")
            val date = LocalDateTime.now().format(dateFormat)
            val file =
                File(Environment.getExternalStorageDirectory().path +
                        "/Download/export_data_${date}.csv")

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