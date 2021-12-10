package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vkpi.touristapp.R
import com.vkpi.touristapp.lists.SavedPlaceListAdapter
import com.vkpi.touristapp.lists.SwipeToDeleteCallback
import com.vkpi.touristapp.ui.MainActivity
import com.vkpi.touristapp.utils.showMessage
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import com.vkpi.touristapp.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved) {
    lateinit var savedPlaceListAdapter: SavedPlaceListAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var placeViewModel: PlaceViewModel
    private val userViewModel by activityViewModels<UserViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBottomNavigationBar()
        placeViewModel = ViewModelProvider(requireActivity()).get(PlaceViewModel::class.java)
        userViewModel.userIdLiveData.value?.let {
            placeViewModel.applyUserPlaces(it)
        }
        setupRecyclerView(view)
        setupObserver()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.saved_place_list)
        savedPlaceListAdapter = SavedPlaceListAdapter()
        recyclerView.adapter = savedPlaceListAdapter
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.adapterPosition
                val item = savedPlaceListAdapter.getPlace(index)
                placeViewModel.deletePlaceFromDb(item)
                savedPlaceListAdapter.removeItem(index)
                requireView().showMessage(getString(R.string.deleted_item_message))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun setupObserver() {
        placeViewModel.userPlacesLiveData.observe(viewLifecycleOwner) {
            savedPlaceListAdapter.submitList(it)
        }
    }
}