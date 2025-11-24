package com.shortcut.myapplication.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface XkcdApi {

    @GET("info.0.json")
    suspend fun getLatestComic(): ComicDto

    @GET("{num}/info.0.json")
    suspend fun getComic(@Path("num") num: Int): ComicDto

    companion object {
        const val BASE_URL = "https://xkcd.com/"
    }
}