package com.mentalys.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinics")
data class ClinicEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val vicinity: String,
    val openNow: Boolean,
    val reference: String,
    val rating: Double,
    val photoUrl: String

)