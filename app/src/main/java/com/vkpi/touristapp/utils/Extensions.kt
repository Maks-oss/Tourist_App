package com.vkpi.touristapp.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.vkpi.touristapp.R


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

fun GoogleMap.createMarker(latLng: LatLng, text: String,action:(Marker)->Unit) {
    val marker = addMarker(
        MarkerOptions()
            .position(latLng)
            .anchor(0.5f, 0.5f)
            .title(text)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
    )
    setOnInfoWindowClickListener {
        action(it)
    }
}
