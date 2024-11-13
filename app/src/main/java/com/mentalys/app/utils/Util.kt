package com.mentalys.app.utils

import android.content.Context
import android.widget.Toast
import com.mentalys.app.data.remote.response.article.Author
import com.mentalys.app.data.remote.response.article.Content
import com.mentalys.app.data.remote.response.article.Metadata
import com.mentalys.app.data.local.entity.AuthorEntity
import com.mentalys.app.data.local.entity.ContentEntity
import com.mentalys.app.data.local.entity.MetadataEntity

fun mapAuthorToEntity(author: Author): AuthorEntity {
    return AuthorEntity(
        id = author.id,
        name = author.name,
        profileImage = author.profileImage,
        bio = author.bio
    )
}

fun mapMetadataToEntity(metadata: Metadata): MetadataEntity {
    return MetadataEntity(
        publishDate = metadata.publishDate,
        lastUpdated = metadata.lastUpdated,
        tags = metadata.tags,
        category = metadata.category,
        readingTime = metadata.readingTime,
        likes = metadata.likes,
        views = metadata.views
    )
}

fun mapContentListToEntity(contentList: List<Content>): List<ContentEntity> {
    return contentList.map { content ->
        ContentEntity(
            type = content.type,
            level = content.level,
            text = content.text,
            src = content.src,
            caption = content.caption,
            alText = content.altText,
            author = content.author,
            authorRole = content.authorRole,
            style = content.style,
            items = content.items,
            platform = content.platform,
            url = content.url,
            description = content.description
        )
    }
}

fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
