package com.vkpi.touristapp

import com.vkpi.touristapp.data.*

object DataSupplier {
    private fun getProperties(rate: Int,name:String) = Properties(
        0.0,
        "kind",
        name,
        "0",
        rate, "", "test"
    )

    fun getFeature(rete:Int,name:String) = Feature(
        geometry = Geometry(listOf(0.0, 0.0), type = "test"),
        id = "test",
        properties = getProperties(rete,name),type = "test"
    )
    fun getFakePlaceDetail() = PlaceDetail(
        null,
        null,
        "",
        "",
        "",
        "",
        "",
        Point(0.0, 0.0),
        null,
        "",
        null,
        "",
        "",
        null,
        "test"
    )

}