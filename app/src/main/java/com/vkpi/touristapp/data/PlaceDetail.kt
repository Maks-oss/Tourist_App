package com.vkpi.touristapp.data

import android.util.Log

data class PlaceDetail(
    val address: Address?,
    val bbox: Bbox?,
    val image: String,
    val kinds: String,
    val name: String,
    val osm: String,
    val otm: String,
    val point: Point,
    val preview: Preview?,
    val rate: String,
    val sources: Sources?,
    val wikidata: String,
    val wikipedia: String,
    val wikipedia_extracts: WikipediaExtracts?,
    val xid: String
){
    override fun hashCode(): Int {
        return xid.hashCode()
    }
}