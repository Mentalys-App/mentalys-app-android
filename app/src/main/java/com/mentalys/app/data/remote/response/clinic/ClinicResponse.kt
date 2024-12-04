package com.mentalys.app.data.remote.response.clinic

import com.mentalys.app.data.local.entity.ClinicEntity

data class ClinicResponse(
    val status: String,
    val data: List<Place>
)

data class Place(
    val name: String,
    val vicinity: String,
    val opening_hours: OpeningHours?,
    val photos: List<Photo>?,
    val reference: String,
    val rating: Int,
    val phoot_url: String,
    ) {
    fun toClinicEntity(): ClinicEntity {
        return ClinicEntity(
            name = this.name,
            vicinity = this.vicinity,
            openNow = this.opening_hours?.open_now ?: false,
            photoReference = this.photos?.firstOrNull()?.photo_reference,
            reference = this.reference,
            rating = this.rating,
            photoUrl = this.phoot_url
        )
    }
}

data class OpeningHours(
    val open_now: Boolean
)

data class Photo(
    val photo_reference: String
)


