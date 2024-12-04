package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "handwriting_tests")
data class HandwritingTestEntity(
    @PrimaryKey val id: String,
    val inputData: String?,
    val metadata: String?,
    val predictionResult: String,
    val confidencePercentage: String,
    val timestamp: String
)