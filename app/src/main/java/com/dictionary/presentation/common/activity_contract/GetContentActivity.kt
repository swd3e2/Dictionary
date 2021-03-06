package com.dictionary.presentation.common.activity_contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

class GetContentActivity : ActivityResultContract<String, Uri?>() {
    @CallSuper
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_GET_CONTENT)
            .setType(input)
            .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/csv", "text/comma-separated-values"))
    }

    override fun getSynchronousResult(
        context: Context,
        input: String
    ): SynchronousResult<Uri?>? = null

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }
}