package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "specialists")
@TypeConverters(Converters::class)
data class SpecialistEntity(
    @PrimaryKey val id: String,
    val fullName: String?,
    val title: String?,
    val mainRole: String?,
    val specializations: List<String?>?,
    val ratings: Double?,
    val reviewCount: Int?,
    val patientsCount: Int?,
    val experienceYears: Int?,
    val aboutMe: String?,
    val languages: List<String?>?,
    val photoUrl: String?,
    val education: List<SpecialistEducationEntity?>?,
    val workingHours: List<SpecialistWorkingHourEntity?>?,
    val certifications: List<String?>?,
    val location: SpecialistLocationEntity?,
    val consultationFee: Int?,
    val features: SpecialistFeaturesEntity?,
    val availability: List<SpecialistAvailabilityEntity?>?,
    val contact: SpecialistContactEntity?
)

data class SpecialistEducationEntity(
    val degree: String?,
    val institution: String?,
    val yearOfGraduation: Int?
)

data class SpecialistWorkingHourEntity(
    val day: String?,
    val startTime: String?,
    val endTime: String?
)

data class SpecialistLocationEntity(
    val address: String?,
    val latitude: String?,
    val longitude: String?
)

data class SpecialistFeaturesEntity(
    val bookAppointment: Boolean?,
    val chat: Boolean?,
    val videoConsultation: Boolean?
)

data class SpecialistAvailabilityEntity(
    val date: String?,
    val timeSlots: List<String>?
)

data class SpecialistContactEntity(
    val phoneNumber: String?,
    val email: String?
)