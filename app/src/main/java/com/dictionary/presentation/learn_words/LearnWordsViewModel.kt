package com.dictionary.presentation.learn_words

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dictionary.domain.entity.Word
import com.dictionary.domain.repository.WordRepository
import com.dictionary.presentation.common.LearnWordsSavedStateHandler
import com.dictionary.presentation.common.Settings
import com.dictionary.presentation.learn_words.state.*
import com.dictionary.utils.UiEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class LearnWordsViewModel @Inject constructor(
    private val wordsRepository: WordRepository,
    private val settings: Settings,
    private val savedStateHandle: SavedStateHandle,
    private val learnWordsSavedStateHandler: LearnWordsSavedStateHandler,
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Current words to learn
     */
    var currentWords = mutableStateListOf<Word>()

    /**
     * Current learn step
     */
    val currentStep = mutableStateOf(1)

    var isLoading = mutableStateOf(false)
        private set

    /**
     * Does view model have state to recover from
     */
    var hasSavedState = mutableStateOf(true)
        private set

    /**
     * Progress of learning
     */
    var learnProgress = mutableStateOf(0f)
        private set

    /**
     * State of match game
     */
    var matchState = MatchState()

    /**
     * State of test game
     */
    var testState = TestState()

    /**
     * State of cards game
     */
    var cardsState = CardsState()

    /**
     * State of write game
     */
    var writeState = WriteState()

    /**
     * Does current word not guessed right in any game
     * Used in progress update
     */
    private var currentWordError = false

    private val recoveredWords = mutableListOf<Word>()

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if (learnWordsSavedStateHandler != null && learnWordsSavedStateHandler.hasSavedState(id)) {
            val savedState =
                learnWordsSavedStateHandler.getSavedState(id)
            savedState?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    val words = getRecoveredWordsToLearn(it.currentWords)
                    withContext(Dispatchers.Main) {
                        recoveredWords.addAll(words)

                        if (recoveredWords.isEmpty()) {
                            startNewWithoutSavedState()
                            hasSavedState.value = false
                            return@withContext
                        }

                        hasSavedState.value = true
                    }
                }
            }
        } else {
            startNewWithoutSavedState()
        }
    }

    fun onEvent(event: LearnWordsEvent) {
        when (event) {
            is LearnWordsEvent.OnGoToMatch -> {
                learnProgress.value = 0.0f
                currentStep.value = 2
                matchState.init(currentWords)
            }
            is LearnWordsEvent.OnMatchSelect -> onMatchSelect(event)
            is LearnWordsEvent.OnGoToTest -> {
                learnProgress.value = 0.25f
                currentStep.value = 3
                testState.init(currentWords)
            }
            is LearnWordsEvent.OnTestSelect -> onTestSelect(event)
            is LearnWordsEvent.OnGoToCards -> {
                learnProgress.value = 0.5f
                currentStep.value = 4
                cardsState.init(currentWords)
            }
            is LearnWordsEvent.OnCardLeftSwipe -> {
                cardsState.doesNotKnowWord()
                cardsState.selectNext()
            }
            is LearnWordsEvent.OnCardRightSwipe -> {
                addProgress()
                if (cardsState.canEnd()) {
                    cardsState.clear()
                    onEvent(LearnWordsEvent.OnGoToWrite)
                    return
                }
                cardsState.selectNext()
            }
            is LearnWordsEvent.OnGoToWrite -> {
                learnProgress.value = 0.75f
                currentStep.value = 5
                writeState.init(currentWords)
            }
            is LearnWordsEvent.OnWriteTryDefinition -> {
                val guessedRight = writeState.tryGuess()
                if (guessedRight) {
                    addProgress()
                } else {
                    currentWordError = true
                }

                if (guessedRight && !writeState.selectNext()) {
                    writeState.clear()
                    onEvent(LearnWordsEvent.OnGoToDone)
                    return
                }
            }
            is LearnWordsEvent.OnGoToDone -> {
                learnProgress.value = 1.0f
                viewModelScope.launch(Dispatchers.IO) {
                    currentWords.forEach { word ->
                        wordsRepository.save(word.apply {
                            bucket = 1
                            firstLearned = Date()
                            lastRepeated = Date()
                        })
                    }
                }
                currentStep.value = 6
            }
            is LearnWordsEvent.OnStartNew -> startNewWithoutSavedState()
            is LearnWordsEvent.OnBack -> {
                if (event.end || currentStep.value <= 1) {
                    deleteSavedState()
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.PopBackStack)
                    }
                    return
                }
                saveState()

                viewModelScope.launch {
                    _uiEvent.send(UiEvent.PopBackStack)
                }
            }
            is LearnWordsEvent.OnContinue -> recoverFromSavedState()
            is LearnWordsEvent.OnRestart -> startNewWithoutSavedState()
        }
    }

    /**
     * Actions on test select game
     */
    private fun onTestSelect(event: LearnWordsEvent.OnTestSelect) {
        when (testState.onSelect(event.selected)) {
            is TestState.State.SelectRight -> {
                testState.setSuccessState(event.selected.index)
                viewModelScope.launch {
                    delay(200)
                    testState.selectNext()
                }
                addProgress()
            }
            is TestState.State.SelectWrong -> {
                testState.setErrorState(event.selected.index)
                viewModelScope.launch {
                    delay(200)
                    testState.setDeselectedState(event.selected.index)
                }
                currentWordError = true
            }
            is TestState.State.GameEnd -> {
                addProgress()
                testState.clear()
                onEvent(LearnWordsEvent.OnGoToCards)
                return
            }
        }
    }

    /**
     * Actions on match select game
     */
    private fun onMatchSelect(event: LearnWordsEvent.OnMatchSelect) {
        val state = matchState.onWordSelect(event.word)
        val word = event.word
        when (state) {
            is MatchState.State.WordSelected -> {
                matchState.setSelectedState(word.index)
            }
            is MatchState.State.WordDeselected -> {
                matchState.setDeselectedState(word.index)
            }
            is MatchState.State.MatchWrong -> {
                val first = state.first
                val second = state.second

                matchState.setErrorState(first.index, second.index)

                viewModelScope.launch {
                    delay(500)
                    matchState.setDeselectedState(first.index, second.index)
                }
            }
            is MatchState.State.MatchRight -> {
                addProgress()
                val first = state.first
                val second = state.second

                matchState.setSuccessState(first.index, second.index)

                viewModelScope.launch {
                    delay(100)
                    matchState.setHideState(first.index, second.index)
                    if (matchState.canGoNextGroup()) {
                        matchState.resetSuccessCount()

                        if (!matchState.selectNextGroup()) {
                            matchState.clear()
                            onEvent(LearnWordsEvent.OnGoToTest)
                        }
                    }
                }
            }
        }
    }

    private fun deleteSavedState() {
        learnWordsSavedStateHandler.clearSavedState(savedStateHandle.get<Int>("id")!!)
    }

    /**
     * Save state of view model to recover from in future
     */
    private fun saveState() {
        val id = savedStateHandle.get<Int>("id")!!
        val gson = Gson()
        learnWordsSavedStateHandler.savedStateHandler.set(
            "saved$id", gson.toJson(
                LearnWordsSavedState(
                    currentStep = currentStep.value,
                    currentWords = currentWords.map { it.id },
                    lastWordWrong = currentWordError,
                    matchSaveState = matchState.toSaveState(),
                    testSaveState = testState.toSaveState(),
                    cardsSaveState = cardsState.toSaveState(),
                    writeSaveState = writeState.toSaveState(),
                )
            )
        )
    }

    /**
     * Load words without recovering from saved state
     */
    private fun startNewWithoutSavedState() {
        hasSavedState.value = false
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val words = getWordsToLearn()
            withContext(Dispatchers.Main) {
                currentWords.clear()
                currentWords.addAll(words)
                currentWords.shuffle()
                isLoading.value = false
                learnProgress.value = 0f
                currentStep.value = 1
            }
        }
    }

    /**
     * Recover learn view model progress from saved state
     */
    private fun recoverFromSavedState() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.Main) {
            val savedState =
                learnWordsSavedStateHandler.getSavedState(savedStateHandle.get<Int>("id")!!)!!

            val wordsMap = getWordsMap(recoveredWords)
            savedState.currentWords.forEach {
                if (wordsMap.containsKey(it))
                    currentWords.add(wordsMap[it]!!)
            }
            currentStep.value = savedState.currentStep

            when (currentStep.value) {
                2 -> {
                    matchState.reinitializeFromSavedState(
                        savedState = savedState.matchSaveState,
                        wordsMap = wordsMap
                    )
                    if (matchState.canGoNextGroup()) {
                        matchState.resetSuccessCount()

                        if (!matchState.selectNextGroup()) {
                            matchState.clear()
                            learnProgress.value = 0.25f
                            onEvent(LearnWordsEvent.OnGoToTest)
                        }
                    } else {
                        learnProgress.value = matchState.getProgress(currentWords.size) / 4f
                    }
                }
                3 -> {
                    testState.reinitializeFromSavedState(
                        savedState = savedState.testSaveState,
                        wordsMap = wordsMap
                    )
                    if (testState.canEnd()) {
                        learnProgress.value = 0.5f
                        onEvent(LearnWordsEvent.OnGoToCards)
                    } else {
                        learnProgress.value = 0.25f + testState.getProgress(currentWords.size) / 4f
                    }
                }
                4 -> {
                    cardsState.reinitializeFromSavedState(
                        savedState = savedState.cardsSaveState,
                        wordsMap = wordsMap
                    )
                    if (cardsState.canEnd()) {
                        learnProgress.value = 0.75f
                        onEvent(LearnWordsEvent.OnGoToCards)
                    } else {
                        learnProgress.value = 0.5f + cardsState.getProgress(currentWords.size) / 4f
                    }
                }
                5 -> {
                    writeState.reinitializeFromSavedState(
                        savedState = savedState.writeSaveState,
                        wordsMap = wordsMap
                    )
                    if (writeState.canEnd()) {
                        learnProgress.value = 1.00f
                        onEvent(LearnWordsEvent.OnGoToDone)
                    } else {
                        learnProgress.value = 0.75f + writeState.getProgress(currentWords.size) / 4f
                    }
                }
            }

            isLoading.value = false
            hasSavedState.value = false
        }
    }

    /**
     * Get words to learn (words who have bucket 0)
     */
    private suspend fun getWordsToLearn(): List<Word> {
        val id = savedStateHandle.get<Int>("id")!!
        return if (id != -1)
            wordsRepository.listByCategoryToLearn(id, settings.countWordsToLearn.value)
        else
            wordsRepository.asListToLearn(settings.countWordsToLearn.value)
    }

    /**
     * Get words to learn (words who have bucket 0)
     */
    private suspend fun getRecoveredWordsToLearn(ids: List<Int>): List<Word> {
        return wordsRepository.listByIds(ids).filter { it.bucket == 0 }
    }

    /**
     * Adds progress if current word was guessed from first try
     * If not it removes current word error
     */
    private fun addProgress() {
        if (currentWordError) {
            currentWordError = false
            return
        }

        learnProgress.value += 100f / currentWords.count() / 4f / 100f
    }

    private fun getWordsMap(words: List<Word>): HashMap<Int, Word> {
        val map = HashMap<Int, Word>()
        words.forEach { word ->
            map[word.id] = word
        }
        return map
    }
}