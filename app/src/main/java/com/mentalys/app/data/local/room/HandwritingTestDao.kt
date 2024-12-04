package com.mentalys.app.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.HandwritingTestEntity

@Dao
interface HandwritingTestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tests: List<HandwritingTestEntity>)

    @Query("SELECT * FROM handwriting_tests ORDER BY timestamp DESC")
    fun getAllHandwritingTests(): PagingSource<Int, HandwritingTestEntity>

    @Query("DELETE FROM handwriting_tests")
    suspend fun clearAll()
}