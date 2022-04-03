package com.dictionary.data.retrofit.dto

data class Translation(
    val asp: String,
    val fr: Int,
    val gen: String,
    val mean: List<Meaning>,
    val pos: String,
    val syn: List<Synonym>,
    val text: String
)