package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.HandwritingEntity
import com.mentalys.app.data.local.entity.QuizEntity

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizHistory(tests: List<QuizEntity>)

    // @Query("SELECT * FROM handwriting_history ORDER BY timestamp DESC")
    // fun getHandwritingHistory(): PagingSource<Int, HandwritingEntity>

    @Query("SELECT * FROM quiz_history ORDER BY timestamp DESC")
    fun getQuizHistory(): LiveData<List<QuizEntity>>

    @Query("DELETE FROM quiz_history")
    suspend fun clearAll()

}