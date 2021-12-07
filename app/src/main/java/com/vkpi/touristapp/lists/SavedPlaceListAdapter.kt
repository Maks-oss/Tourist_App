package com.vkpi.touristapp.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.databinding.PlacesListItemBinding

class SavedPlaceListAdapter :
    RecyclerView.Adapter<SavedPlaceListViewHolder>() {

    private var placeList = mutableListOf<Place>()
//    private val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPlaceListViewHolder {

        val binding =
            PlacesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedPlaceListViewHolder(binding)
    }

    fun getPlace(index: Int) = placeList[index]
    override fun onBindViewHolder(holder: SavedPlaceListViewHolder, position: Int) {
        holder.bind(placeList[position])
    }

    fun submitList(list: List<Place>) {
        placeList=list.toMutableList()
        notifyDataSetChanged()
    }
    fun removeItem(index:Int){
        placeList.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int = placeList.size
}
