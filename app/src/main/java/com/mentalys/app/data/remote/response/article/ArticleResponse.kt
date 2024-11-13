package com.mentalys.app.data.remote.response.article

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("article")
    val article: Article
)

data class Article(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("author")
    val author: Author,

    @SerializedName("metadata")
    val metadata: Metadata,

    @SerializedName("content")
    val content: List<Content>
)

data class Author(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("bio")
    val bio: String,

    @SerializedName("profile_image")
    val profileImage: String
)

data class Metadata(
    @SerializedName("publish_date")
    val publishDate: String,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("tags")
    val tags: List<String>,

    @SerializedName("category")
    val category: String,

    @SerializedName("reading_time")
    val readingTime: Int,

    @SerializedName("likes")
    val likes: Int,

    @SerializedName("views")
    val views: Int
)

data class Content(
    @SerializedName("type")
    val type: String,

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
)
