package com.vkpi.touristapp.lists

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.databinding.PlacesListItemBinding
import com.vkpi.touristapp.ui.fragments.SavedFragmentDirections

class SavedPlaceListViewHolder(private val placesListItemBinding: PlacesListItemBinding) :
    RecyclerView.ViewHolder(placesListItemBinding.root) {
    fun bind(place: Place) {
        placesListItemBinding.apply {
            placeName.text = place.placeName
            placeType.text = place.placeKinds
            placeCard.setOnClickListener {
                it.findNavController().navigate(
                    SavedFragmentDirections.actionSavedFragmentToDetailedPlaceFragment(place.placeId)
                )
            }
        }
    }
}