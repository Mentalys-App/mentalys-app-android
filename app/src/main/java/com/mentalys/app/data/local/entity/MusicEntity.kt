package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musics")
data class MusicItemEntity(
    @PrimaryKey val id: Int,
    val name: String?,
    val tags: List<String?>,
    val license: String?,
    val username: String?
)
