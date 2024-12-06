package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.SpecialistEntity

@Dao
interface SpecialistDao {

    // todo: replace with actual get specialist by id
    @Query("SELECT * FROM specialists WHERE id = :id")
    fun getSpecialist(id: String): LiveData<SpecialistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecialist(article: SpecialistEntity)

    // List of Specialist
    @Query("SELECT * FROM specialists")
    fun getSpecialists(): LiveData<List<SpecialistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecialists(specialist: List<SpecialistEntity>)

}