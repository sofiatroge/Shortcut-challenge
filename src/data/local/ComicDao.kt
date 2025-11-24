package com.shortcut.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDao {

    @Query("SELECT * FROM comics WHERE num = :num")
    suspend fun getComic(num: Int): ComicEntity?

    @Query("SELECT * FROM comics WHERE isFavorite = 1 ORDER BY num DESC")
    fun getFavorites(): Flow<List<ComicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComic(comic: ComicEntity)

    @Query("UPDATE comics SET isFavorite = :isFavorite WHERE num = :num")
    suspend fun updateFavoriteStatus(num: Int, isFavorite: Boolean)

    @Query("SELECT MAX(num) FROM comics")
    suspend fun getMaxComicNumber(): Int?
}