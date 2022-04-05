package com.dictionary.di

import android.app.Application
import androidx.room.Room
import com.dictionary.common.Constants
import com.dictionary.data.retrofit.YandexTranslateApi
import com.dictionary.data.repository.CategoryRepositoryImpl
import com.dictionary.data.repository.TranslationRepositoryImpl
import com.dictionary.data.repository.WordRepositoryImpl
import com.dictionary.data.room.Database
import com.dictionary.domain.repository.CategoryRepository
import com.dictionary.domain.repository.TranslationRepository
import com.dictionary.domain.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideYandexTranslateApi(): YandexTranslateApi {
        return Retrofit.Builder()
            .baseUrl(Constants.YANDEX_TRANSLATE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YandexTranslateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTranslationRepository(api: YandexTranslateApi): TranslationRepository {
        return TranslationRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database {
        return Room.databaseBuilder(
            app, Database::class.java, "dictionary"
        ).allowMainThreadQueries().build()
    }


    @Provides
    @Singleton
    fun provideCategoryRepository(db: Database): CategoryRepository {
        return CategoryRepositoryImpl(db.categoryDao())
    }


    @Provides
    @Singleton
    fun provideWordRepository(db: Database): WordRepository {
        return WordRepositoryImpl(db.wordDao())
    }

}