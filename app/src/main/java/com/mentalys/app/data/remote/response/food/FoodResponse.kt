package com.mentalys.app.data.remote.response.food

import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.FoodEntity

data class FoodResponse(
    @SerializedName("data")
    val data: List<FoodDataResponse>
)

data class FoodDataResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("imageUrl")
    val imageUrl: String

) {
    fun toEntity(): FoodEntity {
        return FoodEntity(
            id = id,
            name = name,
            description = description,
            category = category,
            imageUrl = imageUrl
        )
    }
}
