package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.vkpi.touristapp.R
import com.vkpi.touristapp.databinding.FragmentSearchBinding
import com.vkpi.touristapp.list.PlaceListAdapter
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var fragmentSearchBinding: FragmentSearchBinding
    private val placeViewModel by viewModels<PlaceViewModel>()
    private lateinit var placeListAdapter: PlaceListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return fragmentSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        placeListAdapter = PlaceListAdapter()
        setupObservers()
        fragmentSearchBinding.apply {
            cityInputLayout.setEndIconOnClickListener {
                placeViewModel.applyCity(cityInput.text.toString())
            }
            placesRecyclerView.adapter = placeListAdapter
        }

    }

    private fun setupObservers() {
        placeViewModel.cityLiveData.observe(viewLifecycleOwner) {
            placeViewModel.applyPlaces(it.lat.toString(), it.lon.toString())
        }
        placeViewModel.placesLiveData.observe(viewLifecycleOwner) {
            placeListAdapter.submitList(it.features.filter { feat-> feat.properties.name.isNotEmpty()})
        }
    }
}