package com.mentalys.app.data.remote.response.specialist

import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.SpecialistAvailabilityEntity
import com.mentalys.app.data.local.entity.SpecialistContactEntity
import com.mentalys.app.data.local.entity.SpecialistEducationEntity
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.data.local.entity.SpecialistFeaturesEntity
import com.mentalys.app.data.local.entity.SpecialistLocationEntity
import com.mentalys.app.data.local.entity.SpecialistWorkingHourEntity

data class SpecialistDetailJsonResponse(
    val status: String?,
    val message: String?,
    val data: List<SpecialistDetailsResponse>?
)

data class SpecialistDetailsResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("fullName")
    val fullName: String?,

    @field:SerializedName("title")
    val title: String?,

    @field:SerializedName("mainRole")
    val mainRole: String?,

    @field:SerializedName("specializations")
    val specializations: List<String>?,

    @field:SerializedName("ratings")
    val ratings: Double?,

    @field:SerializedName("reviewCount")
    val reviewCount: Int?,

    @field:SerializedName("patientsCount")
    val patientsCount: Int?,

    @field:SerializedName("experienceYears")
    val experienceYears: Int?,

    @field:SerializedName("aboutMe")
    val aboutMe: String?,

    @field:SerializedName("languages")
    val languages: List<String>?,

    @field:SerializedName("photoUrl")
    val photoUrl: String?,

    @field:SerializedName("education")
    val education: List<SpecialistEducationResponse>?,

    @field:SerializedName("workingHours")
    val workingHours: List<SpecialistWorkingHourResponse>?,

    @field:SerializedName("certifications")
    val certifications: List<String>?,

    @field:SerializedName("location")
    val location: SpecialistLocationResponse?,

    @field:SerializedName("consultationFee")
    val consultationFee: Int?,

    @field:SerializedName("features")
    val features: SpecialistFeaturesResponse?,

    @field:SerializedName("availability")
    val availability: List<SpecialistAvailabilityResponse>?,

    @field:SerializedName("contact")
    val contact: SpecialistContactResponse?

) {
    fun toEntity(): SpecialistEntity {
        return SpecialistEntity(
            id = this.id,
            fullName = this.fullName,
            title = this.title,
            mainRole = this.mainRole,
            specializations = this.specializations,
            ratings = this.ratings,
            reviewCount = this.reviewCount,
            patientsCount = this.patientsCount,
            experienceYears = this.experienceYears,
            aboutMe = this.aboutMe,
            languages = this.languages,
            photoUrl = this.photoUrl,
            education = this.education?.map { it.toEntity() },
            workingHours = this.workingHours?.map { it.toEntity() },
            certifications = this.certifications,
            location = this.location?.toEntity(),
            consultationFee = this.consultationFee,
            features = this.features?.toEntity(),
            availability = this.availability?.map { it.toEntity() },
            contact = this.contact?.toEntity()
        )
    }
}

data class SpecialistEducationResponse(

    @field:SerializedName("degree")
    val degree: String?,

    @field:SerializedName("institution")
    val institution: String?,

    @field:SerializedName("yearOfGraduation")
    val yearOfGraduation: Int?

) {
    fun toEntity(): SpecialistEducationEntity {
        return SpecialistEducationEntity(
            degree = degree,
            institution = institution,
            yearOfGraduation = yearOfGraduation
        )
    }
}

data class SpecialistWorkingHourResponse(

    @field:SerializedName("day")
    val day: String?,

    @field:SerializedName("startTime")
    val startTime: String?,

    @field:SerializedName("endTime")
    val endTime: String?

) {
    fun toEntity(): SpecialistWorkingHourEntity {
        return SpecialistWorkingHourEntity(
            day = day,
            startTime = startTime,
            endTime = endTime
        )
    }
}

data class SpecialistLocationResponse(

    @field:SerializedName("address")
    val address: String?,

    @field:SerializedName("latitude")
    val latitude: String?,

    @field:SerializedName("longitude")
    val longitude: String?

) {
    fun toEntity(): SpecialistLocationEntity {
        return SpecialistLocationEntity(
            address = address,
            latitude = latitude,
            longitude = longitude
        )
    }
}

data class SpecialistFeaturesResponse(

    @field:SerializedName("bookAppointment")
    val bookAppointment: Boolean?,

    @field:SerializedName("chat")
    val chat: Boolean?,

    @field:SerializedName("videoConsultation")
    val videoConsultation: Boolean?

) {
    fun toEntity(): SpecialistFeaturesEntity {
        return SpecialistFeaturesEntity(
            bookAppointment = bookAppointment,
            chat = chat,
            videoConsultation = videoConsultation
        )
    }

}

data class SpecialistAvailabilityResponse(

    @field:SerializedName("date")
    val date: String?,

    @field:SerializedName("timeSlots")
    val timeSlots: List<String>?

) {
    fun toEntity(): SpecialistAvailabilityEntity {
        return SpecialistAvailabilityEntity(
            date = date,
            timeSlots = timeSlots
        )
    }

}

data class SpecialistContactResponse(

    @field:SerializedName("phoneNumber")
    val phoneNumber: String?,

    @field:SerializedName("email")
    val email: String?

) {
    fun toEntity(): SpecialistContactEntity {
        return SpecialistContactEntity(
            phoneNumber = phoneNumber,
            email = email
        )
    }
}
