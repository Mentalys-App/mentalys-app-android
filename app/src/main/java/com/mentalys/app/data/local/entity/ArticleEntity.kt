package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authorEntity: AuthorEntity,
    val metadataEntity: MetadataEntity,
    val contentEntity: List<ContentEntity>
)

data class AuthorEntity(
    val name: String,
    val id: String,
    val profileImage: String,
    val bio: String
)

data class MetadataEntity(
    val publishDate: String,
    val lastUpdated: String,
    val tags: List<String>,
    val category: String,
    val readingTime: Int,
    val likes: Int,
    val views: Int
)

data class ContentEntity(
    val type: String,
    val level: Int? = null,
    val text: String? = null,
    val src: String? = null,
    val caption: String? = null,
    val alText: String? = null,
    val style: String? = null,
    val items: List<String>? = null,
    val platform: String? = null,
    val url: String? = null,
    val description: String? = null,
    val author: String? = null,
    val authorRole: String? = null
)
