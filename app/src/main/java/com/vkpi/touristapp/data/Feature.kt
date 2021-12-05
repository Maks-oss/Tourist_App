package com.vkpi.touristapp.data

data class Feature(
    val geometry: Geometry,
    val id: String,
    val properties: Properties,
    val type: String
)