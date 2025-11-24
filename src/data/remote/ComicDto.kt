package com.shortcut.myapplication.data.remote

import com.google.gson.annotations.SerializedName

data class ComicDto(
    @SerializedName("num") val num: Int,
    @SerializedName("title") val title: String,
    @SerializedName("img") val img: String,
    @SerializedName("alt") val alt: String,
    @SerializedName("day") val day: String,
    @SerializedName("month") val month: String,
    @SerializedName("year") val year: String
)