package com.shortcut.myapplication.di

import android.content.Context
import androidx.room.Room
import com.shortcut.myapplication.data.local.ComicDao
import com.shortcut.myapplication.data.local.ComicDatabase
import com.shortcut.myapplication.data.remote.XkcdApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideXkcdApi(): XkcdApi {
        return Retrofit.Builder()
            .baseUrl(XkcdApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XkcdApi::class.java)
    }

    @Provides
    @Singleton
    fun provideComicDatabase(@ApplicationContext context: Context): ComicDatabase {
        return Room.databaseBuilder(
            context,
            ComicDatabase::class.java,
            "comic_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideComicDao(database: ComicDatabase): ComicDao {
        return database.comicDao()
    }
}