package com.mentalys.app.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mentalys.app.data.local.entity.AuthorEntity
import com.mentalys.app.data.local.entity.ContentEntity
import com.mentalys.app.data.local.entity.MetadataEntity

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromContentList(value: List<ContentEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toContentList(value: String): List<ContentEntity> {
        return Gson().fromJson(value, Array<ContentEntity>::class.java).toList()
    }

    @TypeConverter
    fun fromMetadata(value: MetadataEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMetadata(value: String): MetadataEntity {
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
}
