package com.vkpi.touristapp.list

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.vkpi.touristapp.data.Feature
import com.vkpi.touristapp.databinding.PlacesListItemBinding

class PlaceListViewHolder(private val placesListItemBinding: PlacesListItemBinding) :
    RecyclerView.ViewHolder(placesListItemBinding.root) {
        fun bind (place:Feature){
//            Glide.with(placesListItemBinding.root).load(place.)
            placesListItemBinding.apply {
                placeName.text=place.properties.name
                placeType.text=place.properties.kinds
            }
        }
}