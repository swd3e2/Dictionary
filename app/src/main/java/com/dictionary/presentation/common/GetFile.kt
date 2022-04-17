package com.dictionary.presentation.common

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GetFile(private val registry : ActivityResultRegistry): DefaultLifecycleObserver {
    lateinit var getContent : ActivityResultLauncher<String>

    private val _filenameStateFlow = MutableStateFlow<Uri?>(null)
    val filenameStateFlow = _filenameStateFlow.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("key", owner, GetContent()) { uri ->
            uri?.let {
                _filenameStateFlow.value = it
            }
        }
    }

    fun selectFile() {
        getContent.launch("text/*")
    }
}