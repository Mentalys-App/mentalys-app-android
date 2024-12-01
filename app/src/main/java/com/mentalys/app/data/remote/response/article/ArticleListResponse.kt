package com.mentalys.app.data.remote.response.article

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.ArticleListAuthorEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.ArticleListMetadataEntity

data class ArticleListResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: ArticleListData

)

data class ArticleListData(

    @field:SerializedName("count")
    val count: Int?,

    @field:SerializedName("articles")
    val articles: List<ArticleListItem>

)

data class ArticleListItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String?,

    @field:SerializedName("metadata")
    val metadata: ArticleListMetadata?,

    @field:SerializedName("author")
    val author: ArticleListAuthor?

) {
    // Convert Response to Entity
    fun toEntity(): ArticleListEntity {
        return ArticleListEntity(
            id = this.id,
            title = this.title,
            metadata = this.metadata?.toEntity(), // Converting metadata to Entity
            author = this.author?.toEntity()
        )
    }
}

data class ArticleListMetadata(

    @field:SerializedName("publish_date")
    val publishDate: String?,

    @field:SerializedName("last_updated")
    val lastUpdated: String?,

    @field:SerializedName("tags")
    val tags: List<String?>?,

    @field:SerializedName("category")
    val category: String?,

    @field:SerializedName("reading_time")
    val readingTime: Int?,

    @field:SerializedName("likes")
    val likes: Int?,

    @field:SerializedName("views")
    val views: Int?,

    @field:SerializedName("mental_state")
    val mentalState: String?,

    @field:SerializedName("image_link")
    val imageLink: String?,

    @field:SerializedName("short_description")
    val shortDescription: String?

) {
    // Convert Response Metadata to Entity Metadata
    fun toEntity(): ArticleListMetadataEntity {
        return ArticleListMetadataEntity(
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
data class ArticleListAuthor(

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("id")
    val id: String?,

    @field:SerializedName("profile_image")
    val profileImage: String?,

    @field:SerializedName("bio")
    val bio: String?

) {
    // Convert Response Author to Entity Author
    fun toEntity(): ArticleListAuthorEntity {
        return ArticleListAuthorEntity(
            name = this.name,
            id = this.id,
            profileImage = this.profileImage,
            bio = this.bio
        )
    }
}