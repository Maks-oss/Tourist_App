package com.vkpi.touristapp.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.android.material.chip.Chip
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.vkpi.touristapp.R


fun Context.createChip(text: String) = Chip(this).apply {
    setText(text)
    setBackgroundColor(R.attr.colorPrimary)
    isCheckable = true;
    setTextColor(R.attr.colorOnPrimary)
}
