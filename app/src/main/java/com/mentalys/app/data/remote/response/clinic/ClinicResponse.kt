package com.mentalys.app.data.remote.response.clinic

import com.mentalys.app.data.local.entity.ClinicEntity

data class ClinicResponse(
    val status: String,
    val data: List<PlaceDetails>
)

data class PlaceDetails(
    val businessStatus: String? = null,
    val geometry: Geometry? = null,
    val icon: String? = null,
    val iconBackgroundColor: String? = null,
    val iconMaskBaseUri: String? = null,
    val name: String,
    val openingHours: OpeningHours? = null,
    val placeId: String? = null,
    val plusCode: PlusCode? = null,
    val rating: Double,
    val reference: String,
    val scope: String? = null,
    val types: List<String>? = null,
    val userRatingsTotal: Int? = null,
    val vicinity: String,
    val photos: List<Photo>? = null,
    val photoUrl: String?= null
) { fun toClinicEntity(): ClinicEntity {
        return ClinicEntity(
            name = this.name,
            vicinity = this.vicinity,
            openNow = this.openingHours?.openNow ?: false,
            photoReference = this.photos?.firstOrNull()?.photoReference,
            reference = this.reference,
            rating = this.rating,
        )
    }
}

data class Geometry(
    val location: Location? = null,
    val viewport: Viewport? = null
)

data class Location(
    val lat: Double? = null,
    val lng: Double? = null
)

data class Viewport(
    val northeast: Location? = null,
    val southwest: Location? = null
)

data class OpeningHours(
    val openNow: Boolean? = null
)

data class PlusCode(
    val compoundCode: String? = null,
    val globalCode: String? = null
)

data class Photo(
    val height: Int? = null,
    val htmlAttributions: List<String>? = null,
    val photoReference: String? = null,
    val width: Int? = null
)