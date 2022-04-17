package com.dictionary.presentation.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class GetImageActivity : ActivityResultContract<String, Uri?>() {
    override fun createIntent(context: Context, input: String) = Intent(Intent.ACTION_PICK).setType(input)

    override fun parseResult(resultCode: Int, intent: Intent?) : Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.data
    }
}