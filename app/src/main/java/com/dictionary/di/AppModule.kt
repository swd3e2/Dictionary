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
import com.dictionary.domain.use_case.create_category.CreateCategoryUseCase
import com.dictionary.domain.use_case.delete_category.DeleteCategoryUseCase
import com.dictionary.domain.use_case.get_categories_list.GetCategoriesListUseCase
import com.dictionary.domain.use_case.get_category.GetCategoryUseCase
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
    fun provideGetCategoriesListUseCase(repository: CategoryRepository): GetCategoriesListUseCase {
        return GetCategoriesListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCategoryUseCaseUseCase(repository: CategoryRepository): GetCategoryUseCase {
        return GetCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteCategoryUseCase(repository: CategoryRepository): DeleteCategoryUseCase {
        return DeleteCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateCategoryUseCase(repository: CategoryRepository): CreateCategoryUseCase {
        return CreateCategoryUseCase(repository)
    }

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