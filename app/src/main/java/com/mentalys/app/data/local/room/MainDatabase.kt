package com.mentalys.app.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.FoodEntity
import com.mentalys.app.data.local.entity.HandwritingEntity
import com.mentalys.app.data.local.entity.QuizEntity
import com.mentalys.app.data.local.entity.VoiceEntity
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryEntity
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.utils.Converters

@Database(
    entities = [
        ArticleEntity::class,
        ArticleListEntity::class,
        FoodEntity::class,
        SpecialistEntity::class,
        HandwritingHistoryEntity::class,
        HandwritingEntity::class,
        VoiceEntity::class,
        QuizEntity::class
        // RemoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun mentalHistoryDao(): MentalHistoryDao
    abstract fun specialistDao(): SpecialistDao
    abstract fun voiceDao(): VoiceDao
    abstract fun quizDao(): QuizDao
    // abstract fun remoteDao(): RemoteDao

    companion object {
        @Volatile
        private var instance: MainDatabase? = null
        fun getInstance(context: Context): MainDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java, "Article.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}