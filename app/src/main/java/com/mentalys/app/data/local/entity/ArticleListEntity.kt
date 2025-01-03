package com.mentalys.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.data.remote.response.article.ArticleListAuthor
import com.mentalys.app.data.remote.response.article.ArticleListItem
import com.mentalys.app.data.remote.response.article.ArticleListMetadata
import com.mentalys.app.utils.Converters


@Entity(tableName = "article_list")
@TypeConverters(Converters::class)
data class ArticleListEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "metadata")
    val metadata: ArticleListMetadataEntity?,

    @ColumnInfo(name = "author")
    val author: ArticleListAuthorEntity?

) {
    // Convert Entity to Response Model
    fun toResponse(): ArticleListItem {
        return ArticleListItem(
            id = this.id,
            title = this.title,
            metadata = this.metadata?.toResponse(), // Converting metadata to response model
            author = this.author?.toResponse()
        )
    }
}

@Entity(tableName = "article_list_metadata")
data class ArticleListMetadataEntity(

    @ColumnInfo(name = "publish_date")
    val publishDate: String?,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: String?,

    @ColumnInfo(name = "tags")
    val tags: List<String?>?, // This can be a List, handled by TypeConverters

    @ColumnInfo(name = "category")
    val category: String?,

    @ColumnInfo(name = "reading_time")
    val readingTime: Int?,

    @ColumnInfo(name = "likes")
    val likes: Int?,

    @ColumnInfo(name = "views")
    val views: Int?,

    @ColumnInfo("mental_state")
    val mentalState: String?,

    @ColumnInfo("image_link")
    val imageLink: String?,

    @ColumnInfo("short_description")
    val shortDescription: String?

) {
    // Convert Entity Metadata to Response Metadata
    fun toResponse(): ArticleListMetadata {
        return ArticleListMetadata(
            publishDate = this.publishDate,
            lastUpdated = this.lastUpdated,
            tags = this.tags,
            category = this.category,
            readingTime = this.readingTime,
            likes = this.likes,
            views = this.views,
            mentalState = this.mentalState,
            imageLink = this.imageLink,
            shortDescription = this.shortDescription
        )
    }
}

@Entity(tableName = "article_list_author")
data class ArticleListAuthorEntity(

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "id")
    val id: String?,

    @ColumnInfo(name = "profile_image")
    val profileImage: String?,

    @ColumnInfo(name = "bio")
    val bio: String?,

) {
    // Convert Entity Author to Response Author
    fun toResponse(): ArticleListAuthor {
        return ArticleListAuthor(
            name = this.name,
            id = this.id,
            profileImage = this.profileImage,
            bio = this.bio
        )
    }
}