package com.vkpi.touristapp.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vkpi.touristapp.data.Feature
import com.vkpi.touristapp.databinding.PlacesListItemBinding

class PlaceListAdapter :
    RecyclerView.Adapter<PlaceListViewHolder>() {
    var placesList = mutableListOf<Feature>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListViewHolder {

        val binding =
            PlacesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceListViewHolder, position: Int) {
        holder.bind(placesList[position])
    }
    fun submitList(list:List<Feature>){
        placesList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = placesList.size
}