package com.dictionary.presentation.common.lifecycle_observer

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.dictionary.presentation.common.activity_contract.GetImageActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GetImageLifecycleObserver(private val registry : ActivityResultRegistry): DefaultLifecycleObserver {
    private lateinit var getContent : ActivityResultLauncher<String>

    private val _filenameStateFlow = MutableStateFlow<Uri?>(null)
    val filenameStateFlow = _filenameStateFlow.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("get_image", owner, GetImageActivity()) { uri ->
            uri?.let {
                _filenameStateFlow.value = it
            }
        }
    }

    fun selectImage() {
        getContent.launch("image/*")
    }

    fun reset() {
        _filenameStateFlow.value = null
    }
}