package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.VoiceEntity

@Dao
interface VoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoiceHistory(tests: List<VoiceEntity>)

    // @Query("SELECT * FROM handwriting_history ORDER BY timestamp DESC")
    // fun getHandwritingHistory(): PagingSource<Int, HandwritingEntity>

    @Query("SELECT * FROM voice_history ORDER BY timestamp DESC")
    fun getVoiceHistory(): LiveData<List<VoiceEntity>>

    @Query("DELETE FROM voice_history")
    suspend fun clearAll()

}