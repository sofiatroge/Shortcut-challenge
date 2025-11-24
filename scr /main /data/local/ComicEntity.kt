package com.shortcut.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics")
data class ComicEntity(
    @PrimaryKey val num: Int,
    val title: String,
    val img: String,
    val alt: String,
    val day: String,
    val month: String,
    val year: String,
    val isFavorite: Boolean = false
)