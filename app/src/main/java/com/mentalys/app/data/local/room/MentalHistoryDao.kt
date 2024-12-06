package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryEntity

@Dao
interface MentalHistoryDao {

    // ==================== HANDWRITING ==================== //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHandwritingHistory(tests: List<HandwritingHistoryEntity>)

    @Query("SELECT * FROM handwriting_history ORDER BY timestamp DESC")
    fun getHandwritingHistory(): LiveData<List<HandwritingHistoryEntity>>

    // ==================== QUIZ ==================== //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizHistory(tests: List<QuizHistoryEntity>)

    @Query("SELECT * FROM quiz_history ORDER BY timestamp DESC")
    fun getQuizHistory(): LiveData<List<QuizHistoryEntity>>

    // ==================== VOICE ==================== //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoiceHistory(tests: List<VoiceHistoryEntity>)

    @Query("SELECT * FROM voice_history ORDER BY timestamp DESC")
    fun getVoiceHistory(): LiveData<List<VoiceHistoryEntity>>

    // ==================== CLEAR ==================== //
    @Query("DELETE FROM handwriting_history")
    suspend fun clearHandwritingHistory()

    @Query("DELETE FROM quiz_history")
    suspend fun clearQuizHistory()

    @Query("DELETE FROM voice_history")
    suspend fun clearVoiceHistory()

}