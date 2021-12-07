package com.vkpi.touristapp.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserPlaces(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userPlaceId"
    )
    val places: List<Place>
)