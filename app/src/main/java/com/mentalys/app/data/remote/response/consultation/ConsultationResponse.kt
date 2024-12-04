package com.mentalys.app.data.remote.response.consultation

import com.mentalys.app.data.local.entity.ConsultationEntity

data class ConsultationResponse(
    val id: String,
    val name: String,
    val specialty: String,
    val location: String,
    val consultationFee: Float,
    val contactNumber: String
) {
    fun toEntity(): ConsultationEntity {
        return ConsultationEntity(
            id = id,
            name = name,
            specialty = specialty,
            location = location,
            consultationFee = consultationFee,
            contactNumber = contactNumber
        )
    }
}