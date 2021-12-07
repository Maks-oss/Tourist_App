package com.vkpi.touristapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey val placeId: String,
    val userPlaceId: Long,
    val placeName: String,
    val placeKinds: String,
    val placeImageUrl: String,
    val placeDescription: String
)