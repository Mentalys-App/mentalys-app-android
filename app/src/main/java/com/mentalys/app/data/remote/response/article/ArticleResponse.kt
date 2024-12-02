package com.mentalys.app.data.remote.response.article

import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.ArticleAuthorEntity
import com.mentalys.app.data.local.entity.ArticleContentEntity
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleMetadataEntity

data class ArticleResponse(

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: ArticleDataResponse?

)

data class ArticleDataResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("author")
    val author: ArticleAuthorResponse?,

    @SerializedName("metadata")
    val metadata: ArticleMetadataResponse?,

    @SerializedName("content")
    val content: List<ArticleContentResponse?>

) {
    // Convert Response to Entity Model
    fun toEntity(): ArticleEntity {
        return ArticleEntity(
            id = this.id,
            title = this.title,
            author = this.author?.toEntity(),
            metadata = this.metadata?.toEntity(),
            content = this.content.map { it?.toEntity() }
        )
    }
}

data class ArticleAuthorResponse(

    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("profile_image")
    val profileImage: String?

) {
    // Convert Response to Entity Model
    fun toEntity(): ArticleAuthorEntity {
        return ArticleAuthorEntity(
            id = this.id,
            name = this.name,
            bio = this.bio,
            profileImage = this.profileImage
        )
    }
}

data class ArticleMetadataResponse(

    @SerializedName("publish_date")
    val publishDate: String?,

    @SerializedName("last_updated")
    val lastUpdated: String?,

    @SerializedName("tags")
    val tags: List<String?>?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("reading_time")
    val readingTime: Int?,

    @SerializedName("likes")
    val likes: Int?,

    @SerializedName("views")
    val views: Int?,

    @SerializedName("mental_state")
    val mentalState: String?,

    @SerializedName("image_link")
    val imageLink: String?,

    @SerializedName("short_description")
    val shortDescription: String?

) {
    // Convert Response to Entity Model
    fun toEntity(): ArticleMetadataEntity {
        return ArticleMetadataEntity(
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

data class ArticleContentResponse(

    @SerializedName("type")
    val type: String?,

    @SerializedName("level")
    val level: Int? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("src")
    val src: String? = null,

    @SerializedName("caption")
    val caption: String? = null,

    @SerializedName("alt_text")
    val altText: String? = null,

    @SerializedName("author")
    val author: String? = null,

    @SerializedName("author_role")
    val authorRole: String? = null,

    @SerializedName("style")
    val style: String? = null,

    @SerializedName("items")
    val items: List<String>? = null,

    @SerializedName("platform")
    val platform: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("description")
    val description: String? = null

) {
    // Convert Response to Entity Model
    fun toEntity(): ArticleContentEntity {
        return ArticleContentEntity(
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
