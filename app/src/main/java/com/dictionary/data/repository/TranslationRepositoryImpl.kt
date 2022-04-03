package com.dictionary.data.repository

import com.dictionary.common.Resource
import com.dictionary.data.retrofit.YandexTranslateApi
import com.dictionary.domain.entity.Translation
import com.dictionary.domain.repository.TranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val api: YandexTranslateApi,
) : TranslationRepository {

    override fun getTranslation(word: String): Flow<Resource<Translation>> = flow {
        emit(Resource.Loading<Translation>())

        try {
            val translation = api.getSuggestTranslation(
                "",
                "en-ru",
                word,
            )

            emit(Resource.Success<Translation>(data = translation.toEntity()))
        } catch (e: HttpException) {
            emit(
                Resource.Error<Translation>(
                    message = "Oops, something went wrong!",
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error<Translation>(
                    message = e.localizedMessage,
                )
            )
        }
    }
}