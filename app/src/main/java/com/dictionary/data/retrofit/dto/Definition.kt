package com.dictionary.data.retrofit.dto

data class Definition(
    val pos: String,
    val text: String,
    val tr: List<Translation>,
    val ts: String
)