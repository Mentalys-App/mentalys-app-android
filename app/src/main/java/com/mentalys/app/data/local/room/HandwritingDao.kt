package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.HandwritingEntity

@Dao
interface HandwritingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHandwritingHistory(tests: List<HandwritingEntity>)

    // @Query("SELECT * FROM handwriting_history ORDER BY timestamp DESC")
    // fun getHandwritingHistory(): PagingSource<Int, HandwritingEntity>

    @Query("SELECT * FROM handwriting_history ORDER BY timestamp DESC")
    fun getHandwritingHistory(): LiveData<List<HandwritingEntity>>

    @Query("DELETE FROM handwriting_history")
    suspend fun clearAll()

}