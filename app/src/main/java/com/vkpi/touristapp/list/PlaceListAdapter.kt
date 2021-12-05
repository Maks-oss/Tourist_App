package com.vkpi.touristapp.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vkpi.touristapp.data.Feature
import com.vkpi.touristapp.databinding.PlacesListItemBinding

class PlaceListAdapter :
    RecyclerView.Adapter<PlaceListViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Feature>() {
        override fun areItemsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListViewHolder {

        val binding =
            PlacesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun submitList(list: List<Feature>) {
        val placesList = list.filter { feat -> feat.properties.name.isNotEmpty() }.toMutableList()
        differ.submitList(placesList)
    }

    fun filterList(list: List<Feature>,param: String) {
        val placesList = list.filter { it.properties.kinds.contains(param) }.toMutableList()
        differ.submitList(placesList)
    }

    override fun getItemCount(): Int = differ.currentList.size
}
