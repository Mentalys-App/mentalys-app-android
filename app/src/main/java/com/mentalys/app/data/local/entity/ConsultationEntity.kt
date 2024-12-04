package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultation")
data class ConsultationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: String,
    val location: String,
    val consultationFee: Float,
    val contactNumber: String
)