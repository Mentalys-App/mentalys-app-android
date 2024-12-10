package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.MusicDetailEntity
import com.mentalys.app.data.local.entity.MusicItemEntity
import com.mentalys.app.data.local.entity.SpecialistEntity

@Dao
interface MusicDao {

    @Query("SELECT * FROM music WHERE id = :id")
    fun getMusic(id: Int): LiveData<MusicDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(data: MusicDetailEntity)

    @Query("SELECT * FROM musics")
    fun getMusics(): LiveData<List<MusicItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusics(data: List<MusicItemEntity>)

}