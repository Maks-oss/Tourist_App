package com.vkpi.touristapp.list

import android.util.Log
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vkpi.touristapp.data.Feature
import com.vkpi.touristapp.databinding.PlacesListItemBinding
import com.vkpi.touristapp.ui.fragments.SearchFragmentDirections

class PlaceListViewHolder(private val placesListItemBinding: PlacesListItemBinding) :
    RecyclerView.ViewHolder(placesListItemBinding.root) {
        fun bind (place:Feature){
            placesListItemBinding.apply {
                placeName.text=place.properties.name
                placeType.text=place.properties.kinds
                placeCard.setOnClickListener {
                    it.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailedPlaceFragment(place.properties.xid))
                }
            }
        }
}