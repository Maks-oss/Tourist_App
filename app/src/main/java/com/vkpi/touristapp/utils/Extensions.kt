package com.vkpi.touristapp.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.vkpi.touristapp.R
import com.vkpi.touristapp.data.PlaceDetail
import com.vkpi.touristapp.database.entities.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun Context.createChip(text: String) = Chip(this).apply {
    setText(text)
    setBackgroundColor(R.attr.colorPrimary)
    isCheckable = true;
    setTextColor(R.attr.colorOnPrimary)
}


fun GoogleMap.setupMap(latLng: LatLng, text: String) {
    clear()
    addMarker(
        MarkerOptions().position(
            latLng
        ).title(text)
    )
    animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
}

fun Context.showMessage(text: String) = Toast.makeText(this,text,Toast.LENGTH_SHORT).show()

fun GoogleMap.createMarker(latLng: LatLng, text: String,action:(Marker)->Unit) {
    addMarker(
        MarkerOptions()
            .position(latLng)
            .anchor(0.5f, 0.5f)
            .title(text)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
    )
    setOnInfoWindowClickListener {
        action(it)
    }
}

fun PlaceDetail.getUserPlaceEntity(userId:Long)= Place(
    placeId = xid,
    placeKinds = kinds?:"",
    placeDescription = wikipedia_extracts?.text?:"",
    placeName = name?:"",
    placeImageUrl = preview?.source?: IMAGE_NOT_FOUND_URL,
    userPlaceId = userId
)

fun String.extractMeters() =substringBefore(" ").plus("000")