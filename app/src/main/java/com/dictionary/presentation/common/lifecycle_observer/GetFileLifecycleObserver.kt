package com.dictionary.presentation.common.lifecycle_observer

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.dictionary.presentation.common.activity_contract.GetContentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GetFileLifecycleObserver(private val registry : ActivityResultRegistry): DefaultLifecycleObserver {
    private lateinit var getContent : ActivityResultLauncher<String>

    private val _filenameStateFlow = MutableStateFlow<Uri?>(null)
    val filenameStateFlow = _filenameStateFlow.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("get_file", owner, GetContentActivity()) { uri ->
            uri?.let {
                _filenameStateFlow.value = it
            }
        }
    }

    fun selectFile() {
        getContent.launch("text/*")
    }

    fun reset() {
        _filenameStateFlow.value = null
    }
}