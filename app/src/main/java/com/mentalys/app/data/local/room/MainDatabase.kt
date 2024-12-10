package com.mentalys.app.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.ClinicEntity
import com.mentalys.app.data.local.entity.FoodEntity
import com.mentalys.app.data.local.entity.MusicDetailEntity
import com.mentalys.app.data.local.entity.MusicItemEntity
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryEntity
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryEntity
import com.mentalys.app.utils.Converters

@Database(
    entities = [
        ArticleEntity::class,
        ArticleListEntity::class,
        FoodEntity::class,
        SpecialistEntity::class,
        ClinicEntity::class,
        HandwritingHistoryEntity::class,
        QuizHistoryEntity::class,
        VoiceHistoryEntity::class,
        MusicItemEntity::class,
        MusicDetailEntity::class
        // RemoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun specialistDao(): SpecialistDao
    abstract fun mentalHistoryDao(): MentalHistoryDao
    abstract fun clinicDao(): ClinicDao
    abstract fun musicDao(): MusicDao
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