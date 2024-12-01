package com.mentalys.app.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mentalys.app.data.local.entity.ArticleListMetadataEntity
import com.mentalys.app.data.local.entity.AuthorEntity
import com.mentalys.app.data.local.entity.ContentEntity
import com.mentalys.app.data.local.entity.MetadataEntity
import com.mentalys.app.data.remote.response.article.ArticleListMetadata

class Converters {

//    @TypeConverter
//    fun fromStringList(value: List<String?>?): String {
//        return value?.joinToString(",") { it.orEmpty() } ?: ""
//    }
//
//    @TypeConverter
//    fun toStringList(value: String): List<String?>? {
//        return if (value.isBlank()) null else value.split(",").map { it.ifEmpty { null } }
//    }

    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return value?.let { Gson().fromJson(it, listType) }
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

//    @TypeConverter
//    fun fromStringList(value: List<String>): String {
//        return value.joinToString(",")
//    }
//
//    @TypeConverter
//    fun toStringList(value: String): List<String> {
//        return value.split(",").map { it.trim() }
//    }

    @TypeConverter
    fun fromContentList(value: List<ContentEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toContentList(value: String): List<ContentEntity> {
        return Gson().fromJson(value, Array<ContentEntity>::class.java).toList()
    }

    @TypeConverter
    fun fromMetadata2(value: MetadataEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMetadata2(value: String): MetadataEntity {
        return Gson().fromJson(value, MetadataEntity::class.java)
    }

    @TypeConverter
    fun fromAuthor(value: AuthorEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toAuthor(value: String): AuthorEntity {
        return Gson().fromJson(value, AuthorEntity::class.java)
    }


    private val gson = Gson()

    @TypeConverter
    fun fromMetadata(metadata: ArticleListMetadataEntity?): String? {
        return gson.toJson(metadata)
    }

    @TypeConverter
    fun toMetadata(data: String?): ArticleListMetadataEntity? {
        return gson.fromJson(data, ArticleListMetadataEntity::class.java)
    }

//    @TypeConverter
//    fun fromStringList(list: List<String?>?): String? {
//        return gson.toJson(list)
//    }
//
//    @TypeConverter
//    fun toStringList(data: String?): List<String?>? {
//        val listType = object : TypeToken<List<String?>>() {}.type
//        return gson.fromJson(data, listType)
//    }

}
