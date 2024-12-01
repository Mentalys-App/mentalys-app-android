package com.mentalys.app.data.remote.response.article

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

data class ArticleListResponse(
    val message: String,
    val data: ArticleListData
)

data class ArticleListData(
    val count: Int?,
    val articles: List<ArticleListItem>
)

@Entity(tableName = "articles")
data class ArticleListItem(
    @PrimaryKey val id: String,
    val title: String?,
    @TypeConverters(Converters::class) val metadata: ArticleListMetadata?,
)

data class ArticleListMetadata(
    val publish_date: String?,
    val last_updated: String?,
    @TypeConverters(Converters::class) val tags: List<String?>?,
    val category: String?,
    val reading_time: Int?,
    val likes: Int?,
    val views: Int?
)