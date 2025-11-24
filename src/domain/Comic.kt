package com.shortcut.myapplication.domain

data class Comic(
    val num: Int,
    val title: String,
    val img: String,
    val alt: String,
    val day: String,
    val month: String,
    val year: String,
    val isFavorite: Boolean = false
) {
    val date: String
        get() = "$year-$month-$day"
}