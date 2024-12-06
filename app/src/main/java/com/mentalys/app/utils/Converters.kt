package com.mentalys.app.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mentalys.app.data.local.entity.ArticleAuthorEntity
import com.mentalys.app.data.local.entity.ArticleListAuthorEntity
import com.mentalys.app.data.local.entity.ArticleContentEntity
import com.mentalys.app.data.local.entity.ArticleListMetadataEntity
import com.mentalys.app.data.local.entity.ArticleMetadataEntity
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryPredictionEntity
import com.mentalys.app.data.local.entity.SpecialistAvailabilityEntity
import com.mentalys.app.data.local.entity.SpecialistContactEntity
import com.mentalys.app.data.local.entity.SpecialistEducationEntity
import com.mentalys.app.data.local.entity.SpecialistFeaturesEntity
import com.mentalys.app.data.local.entity.SpecialistLocationEntity
import com.mentalys.app.data.local.entity.SpecialistWorkingHourEntity
//import com.mentalys.app.data.local.entity.QuizHistoryPredictionEntity
//import com.mentalys.app.data.local.entity.VoiceHistoryPredictionEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryInputDataEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryPredictionEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryPredictionResultEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryConfidenceScoresEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryPredictionEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryPredictionResultEntity

class Converters {

    private val gson = Gson()
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
    fun fromContentList(value: List<ArticleContentEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toContentList(value: String): List<ArticleContentEntity> {
        return Gson().fromJson(value, Array<ArticleContentEntity>::class.java).toList()
    }

    @TypeConverter
    fun fromMetadata2(value: ArticleMetadataEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMetadata2(value: String): ArticleMetadataEntity {
        return Gson().fromJson(value, ArticleMetadataEntity::class.java)
    }

    @TypeConverter
    fun fromAuthor(value: ArticleAuthorEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toListAuthor(value: String): ArticleListAuthorEntity {
        return Gson().fromJson(value, ArticleListAuthorEntity::class.java)
    }

    @TypeConverter
    fun fromListAuthor(value: ArticleListAuthorEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toAuthor(value: String): ArticleAuthorEntity {
        return Gson().fromJson(value, ArticleAuthorEntity::class.java)
    }


    @TypeConverter
    fun fromArticleListContentEntity(data: List<ArticleContentEntity?>): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toArticleListContentEntity(data: String?): List<ArticleContentEntity?> {
        return gson.fromJson(data, Array<ArticleContentEntity?>::class.java).toList()
    }

    @TypeConverter
    fun fromArticleContentEntity(data: ArticleContentEntity): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toArticleContentEntity(data: String?): ArticleContentEntity? {
        return gson.fromJson(data, ArticleContentEntity::class.java)
    }

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


    // ============================== MENTAL TEST CONVERTERS ============================== //

    @TypeConverter
    fun fromHandwritingPredictionEntity(data: HandwritingHistoryPredictionEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toHandwritingPredictionEntity(data: String?): HandwritingHistoryPredictionEntity? {
        return gson.fromJson(data, HandwritingHistoryPredictionEntity::class.java)
    }

    // ============================== SPECIALIST CONVERTERS ============================== //

    @TypeConverter
    fun fromSpecialistEducationEntity(data: List<SpecialistEducationEntity?>?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toSpecialistEducationEntity(data: String?): List<SpecialistEducationEntity?>? {
        val type = object : TypeToken<List<SpecialistEducationEntity?>>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun fromSpecialistWorkingHourEntity(data: List<SpecialistWorkingHourEntity?>?): String? {
        val type = object : TypeToken<List<SpecialistWorkingHourEntity?>>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun toSpecialistWorkingHourEntity(data: String?): List<SpecialistWorkingHourEntity?>? {
        val type = object : TypeToken<List<SpecialistWorkingHourEntity?>?>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun fromSpecialistLocationEntity(data: SpecialistLocationEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toSpecialistLocationEntity(data: String?): SpecialistLocationEntity? {
        return gson.fromJson(data, SpecialistLocationEntity::class.java)
    }
//    @TypeConverter
//    fun fromVoicePredictionEntity(metadata: com.mentalys.app.data.local.entity.VoiceHistoryPredictionEntity?): String? {
//        return gson.toJson(metadata)
//    }
//
//    @TypeConverter
//    fun toVoicePredictionEntity(data: String?): com.mentalys.app.data.local.entity.VoiceHistoryPredictionEntity? {
//        return gson.fromJson(data, VoiceHistoryPredictionEntity::class.java)
//    }
//
//    @TypeConverter
//    fun fromQuizPredictionEntity(metadata: com.mentalys.app.data.local.entity.QuizHistoryPredictionEntity?): String? {
//        return gson.toJson(metadata)
//    }
//
//    @TypeConverter
//    fun toQuizPredictionEntity(data: String?): com.mentalys.app.data.local.entity.QuizHistoryPredictionEntity? {
//        return gson.fromJson(data, QuizHistoryPredictionEntity::class.java)
//    }


    @TypeConverter
    fun fromSpecialistFeaturesEntity(data: SpecialistFeaturesEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toSpecialistFeaturesEntity(data: String?): SpecialistFeaturesEntity? {
        return gson.fromJson(data, SpecialistFeaturesEntity::class.java)
    }

    @TypeConverter
    fun fromSpecialistAvailabilityEntity(data: List<SpecialistAvailabilityEntity?>?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toSpecialistAvailabilityEntity(data: String?): List<SpecialistAvailabilityEntity?>? {
        val type = object : TypeToken<List<SpecialistWorkingHourEntity?>?>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun fromSpecialistContactEntity(data: SpecialistContactEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toSpecialistContactEntity(data: String?): SpecialistContactEntity? {
        val type = object : TypeToken<SpecialistContactEntity?>() {}.type
        return gson.fromJson(data, type)
    }


    // ============================== VOICE HISTORY TEST CONVERTERS ============================== //\

    @TypeConverter
    fun fromVoiceHistoryPredictionEntity(data: VoiceHistoryPredictionEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toVoiceHistoryPredictionEntity(data: String?): VoiceHistoryPredictionEntity? {
        return gson.fromJson(data, VoiceHistoryPredictionEntity::class.java)
    }

    @TypeConverter
    fun fromVoiceHistoryPredictionResultEntity(data: VoiceHistoryPredictionResultEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toVoiceHistoryPredictionResultEntity(data: String?): VoiceHistoryPredictionResultEntity? {
        return gson.fromJson(data, VoiceHistoryPredictionResultEntity::class.java)
    }

    @TypeConverter
    fun fromVoiceHistoryConfidenceScoreEntity(data: VoiceHistoryConfidenceScoresEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toVoiceHistoryConfidenceScoreEntity(data: String?): VoiceHistoryConfidenceScoresEntity? {
        return gson.fromJson(data, VoiceHistoryConfidenceScoresEntity::class.java)
    }

    // ============================== QUIZ HISTORY TEST CONVERTERS ============================== //\

    @TypeConverter
    fun fromQuizHistoryPredictionEntity(data: QuizHistoryPredictionEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toQuizHistoryPredictionEntity(data: String?): QuizHistoryPredictionEntity? {
        return gson.fromJson(data, QuizHistoryPredictionEntity::class.java)
    }

    @TypeConverter
    fun fromQuizHistoryInputDataEntity(data: QuizHistoryInputDataEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toQuizHistoryInputDataEntity(data: String?): QuizHistoryInputDataEntity? {
        return gson.fromJson(data, QuizHistoryInputDataEntity::class.java)
    }

    @TypeConverter
    fun fromQuizHistoryPredictionResultEntity(data: QuizHistoryPredictionResultEntity?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toQuizHistoryPredictionResultEntity(data: String?): QuizHistoryPredictionResultEntity? {
        return gson.fromJson(data, QuizHistoryPredictionResultEntity::class.java)
    }

}
