package com.mentalys.app.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mentalys.app.data.local.entity.RemoteEntity
import com.mentalys.app.utils.Converters

//@Database(entities = [HandwritingEntity::class, RemoteEntity::class], version = 2, exportSchema = false)
//@TypeConverters(Converters::class)
//abstract class HistoryDatabase : RoomDatabase() {
//    abstract fun handwritingTestDao(): HandwritingDao
//    abstract fun remoteDao(): RemoteDao
//    companion object {
//        @Volatile
//        private var instance: HistoryDatabase? = null
//        fun getInstance(context: Context): HistoryDatabase =
//            instance ?: synchronized(this) {
//                instance ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    HistoryDatabase::class.java, "History.db"
//                ).fallbackToDestructiveMigration().build()
//            }
//    }
//}