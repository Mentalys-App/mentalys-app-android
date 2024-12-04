package com.mentalys.app.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mentalys.app.data.local.entity.ClinicEntity
import com.mentalys.app.utils.Converters

@Database(
    entities = [ClinicEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ClinicDatabase : RoomDatabase() {
    abstract fun clinicDao(): ClinicDao
    companion object {
        @Volatile
        private var instance: ClinicDatabase? = null
        fun getInstance(context: Context): ClinicDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ClinicDatabase::class.java, "Clinic.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}