package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import androidx.room.ColumnInfo
import com.mentalys.app.data.remote.response.article.ArticleAuthorResponse
import com.mentalys.app.data.remote.response.article.ArticleContentResponse
import com.mentalys.app.data.remote.response.article.ArticleDataResponse
import com.mentalys.app.data.remote.response.article.ArticleMetadataResponse
import com.mentalys.app.utils.Converters

@Entity(tableName = "article")
@TypeConverters(Converters::class)
data class ArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "author") val author: ArticleAuthorEntity?,
    @ColumnInfo(name = "metadata") val metadata: ArticleMetadataEntity?,
    @ColumnInfo(name = "content") val content: List<ArticleContentEntity?>
) {
    fun toResponse(): ArticleDataResponse {
        return ArticleDataResponse(
            id = this.id,
            title = this.title,
            author = this.author?.toResponse(),
            metadata = this.metadata?.toResponse(),
            content = this.content.map { it?.toResponse() }
        )
    }
}

data class ArticleAuthorEntity(
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "bio") val bio: String?,
    @ColumnInfo(name = "profile_image") val profileImage: String?
) {
    fun toResponse(): ArticleAuthorResponse {
        return ArticleAuthorResponse(
            id = this.id,
            name = this.name,
            bio = this.bio,
            profileImage = this.profileImage
        )
    }
}

data class ArticleMetadataEntity(
    @ColumnInfo(name = "publish_date") val publishDate: String?,
    @ColumnInfo(name = "last_updated") val lastUpdated: String?,
    @ColumnInfo(name = "tags") val tags: List<String?>?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "reading_time") val readingTime: Int?,
    @ColumnInfo(name = "likes") val likes: Int?,
    @ColumnInfo(name = "views") val views: Int?,
    @ColumnInfo(name = "mental_state") val mentalState: String?,
    @ColumnInfo(name = "image_link") val imageLink: String?,
    @ColumnInfo(name = "short_description") val shortDescription: String?
) {
    fun toResponse(): ArticleMetadataResponse {
        return ArticleMetadataResponse(
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

data class ArticleContentEntity(
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "level") val level: Int? = null,
    @ColumnInfo(name = "text") val text: String? = null,
    @ColumnInfo(name = "src") val src: String? = null,
    @ColumnInfo(name = "caption") val caption: String? = null,
    @ColumnInfo(name = "alt_text") val altText: String? = null,
    @ColumnInfo(name = "style") val style: String? = null,
    @ColumnInfo(name = "items") val items: List<String>? = null,
    @ColumnInfo(name = "platform") val platform: String? = null,
    @ColumnInfo(name = "url") val url: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "author") val author: String? = null,
    @ColumnInfo(name = "author_role") val authorRole: String? = null
) {
    fun toResponse(): ArticleContentResponse {
        return ArticleContentResponse(
            type = this.type,
            level = this.level,
            text = this.text,
            src = this.src,
            caption = this.caption,
            altText = this.altText,
            style = this.style,
            items = this.items,
            platform = this.platform,
            url = this.url,
            description = this.description,
            author = this.author,
            authorRole = this.authorRole
        )
    }
}
