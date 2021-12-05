package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import com.vkpi.touristapp.R
import com.vkpi.touristapp.data.Feature
import com.vkpi.touristapp.databinding.FragmentSearchBinding
import com.vkpi.touristapp.list.PlaceListAdapter
import com.vkpi.touristapp.utils.Resource
import com.vkpi.touristapp.utils.createChip
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var fragmentSearchBinding: FragmentSearchBinding
    private val placeViewModel by viewModels<PlaceViewModel>()
    @Inject
    lateinit var placeListAdapter: PlaceListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return fragmentSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObservers()
        fragmentSearchBinding.apply {
            cityInputLayout.setEndIconOnClickListener {
                placeViewModel.applyCity(cityInput.text.toString())
            }
            placesRecyclerView.adapter = placeListAdapter
            chipGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId==-1){
                    placeListAdapter.submitList(placeViewModel.placesLiveData.value!!.data!!.features)
                } else {
                    val chip = group.findViewById<Chip>(checkedId)
                    placeListAdapter.filterList(placeViewModel.placesLiveData.value!!.data!!.features,chip.text.toString())
                }
            }
        }

    }

    private fun setupObservers() {
        placeViewModel.cityLiveData.observe(viewLifecycleOwner) {
            placeViewModel.applyPlaces(it.lat.toString(), it.lon.toString())
        }
        placeViewModel.placesLiveData.observe(viewLifecycleOwner) { place ->
            when (place) {
                is Resource.Success -> {
                    val list =
                        place.data?.features!!
                    placeListAdapter.submitList(list)
                    createChips(list)
                    fragmentSearchBinding.apply {
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        fragmentSearchBinding.placesRecyclerView.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    fragmentSearchBinding.apply {
                        placesRecyclerView.visibility = View.GONE
                        shimmerLayout.visibility = View.VISIBLE
                        shimmerLayout.startShimmer()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), place.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    private fun createChips(list:List<Feature>){
        val chipList=list.map { it.properties.kinds.split(",") }.flatten().distinct()
        chipList.forEach { category ->
            fragmentSearchBinding.chipGroup.addView(requireContext().createChip(category))
        }
    }

}