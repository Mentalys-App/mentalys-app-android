package com.mentalys.app.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.ConsultationEntity
import com.mentalys.app.data.local.entity.FoodEntity
import com.mentalys.app.utils.Converters

@Database(
    entities = [
        ArticleEntity::class,
        ArticleListEntity::class,
        FoodEntity::class,
        ConsultationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null
        fun getInstance(context: Context): ArticleDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java, "Article.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}