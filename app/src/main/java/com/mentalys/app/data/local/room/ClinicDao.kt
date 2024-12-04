package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.ClinicEntity

@Dao
interface ClinicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListClinics(clinics: List<ClinicEntity>)

    @Query("SELECT * FROM clinics")
    fun getAllClinics(): LiveData<List<ClinicEntity>>

    @Query("SELECT * FROM clinics ORDER BY rating DESC LIMIT 4")
    fun get4Clinics(): LiveData<List<ClinicEntity>>

    @Query("DELETE FROM clinics")
    suspend fun clearClinics()
}