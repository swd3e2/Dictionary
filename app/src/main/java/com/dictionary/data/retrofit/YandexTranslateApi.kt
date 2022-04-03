package com.dictionary.data.retrofit

import com.dictionary.data.retrofit.dto.TranslationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface YandexTranslateApi {
    @GET("/api/v1/dicservice.json/lookup")
    suspend fun getSuggestTranslation(
        @Query("key") key: String,
        @Query("lang") lang: String,
        @Query("text") text: String,
    ): TranslationDto
}