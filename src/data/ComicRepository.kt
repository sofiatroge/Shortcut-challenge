package com.shortcut.myapplication.data

import com.shortcut.myapplication.data.local.ComicDao
import com.shortcut.myapplication.data.local.ComicEntity
import com.shortcut.myapplication.data.remote.ComicDto
import com.shortcut.myapplication.data.remote.XkcdApi
import com.shortcut.myapplication.domain.Comic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

@Singleton
class ComicRepository @Inject constructor(
    private val api: XkcdApi,
    private val dao: ComicDao
) {

    suspend fun getLatestComic(): Result<Comic> {
        return try {
            val dto = api.getLatestComic()
            val entity = dto.toEntity()
            dao.insertComic(entity)
            Result.Success(entity.toDomain())
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getComic(num: Int): Result<Comic> {
        return try {
            // Try local first
            val local = dao.getComic(num)
            if (local != null) {
                return Result.Success(local.toDomain())
            }

            // Fetch from API
            val dto = api.getComic(num)
            val entity = dto.toEntity()
            dao.insertComic(entity)
            Result.Success(entity.toDomain())
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getRandomComic(): Result<Comic> {
        return try {
            val latest = api.getLatestComic()
            val randomNum = (1..latest.num).random()
            getComic(randomNum)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun toggleFavorite(num: Int) {
        val comic = dao.getComic(num)
        if (comic != null) {
            dao.updateFavoriteStatus(num, !comic.isFavorite)
        }
    }

    fun getFavorites(): Flow<List<Comic>> {
        return dao.getFavorites().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getMaxComicNumber(): Int? {
        return dao.getMaxComicNumber()
    }

    // Mappers
    private fun ComicDto.toEntity() = ComicEntity(
        num = num,
        title = title,
        img = img,
        alt = alt,
        day = day,
        month = month,
        year = year
    )

    private fun ComicEntity.toDomain() = Comic(
        num = num,
        title = title,
        img = img,
        alt = alt,
        day = day,
        month = month,
        year = year,
        isFavorite = isFavorite
    )
}