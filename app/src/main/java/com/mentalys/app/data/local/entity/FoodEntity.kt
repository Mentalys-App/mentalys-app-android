package com.mentalys.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mentalys.app.data.remote.response.food.FoodDataResponse

@Entity(tableName = "food")
data class FoodEntity (

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String

) {
    fun toResponse(): FoodDataResponse {
        return FoodDataResponse(
            id = id,
            name = name,
            description = description,
            category = category,
            imageUrl = imageUrl
        )
    }
}