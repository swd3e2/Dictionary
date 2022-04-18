package com.dictionary.data.retrofit.dto

import com.google.gson.annotations.SerializedName

data class Meaning(
    @SerializedName("text")
    val text: String
)