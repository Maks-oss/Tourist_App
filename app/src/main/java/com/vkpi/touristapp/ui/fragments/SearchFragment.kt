package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import com.vkpi.touristapp.data.Feature
import com.vkpi.touristapp.databinding.FragmentSearchBinding
import com.vkpi.touristapp.list.PlaceListAdapter
import com.vkpi.touristapp.utils.Resource
import com.vkpi.touristapp.utils.createChip
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        setupObservers()
        setupTextInput()
        setupRecyclerView()
        setupChipGroup()
    }

    private fun setupRecyclerView() {
        placeListAdapter = PlaceListAdapter()
        fragmentSearchBinding.placesRecyclerView.adapter = placeListAdapter
    }

    private fun setupChipGroup() {
        fragmentSearchBinding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val featuresList = sortedList(placeViewModel.placesLiveData.value!!.data!!.features)
            if (checkedId == -1) {
                placeListAdapter.submitList(featuresList)
            } else {
                val chip = group.findViewById<Chip>(checkedId)
                placeListAdapter.filterList(
                    featuresList,
                    chip.text.toString()
                )
            }
        }
    }

    private fun setupTextInput() {
        var job: Job? = null
        fragmentSearchBinding.cityInput.addTextChangedListener { input ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(2000)
                input?.let { city ->
                    if (city.toString()
                            .isNotEmpty() && placeViewModel.cityLiveData.value?.name != city.toString()
                    ) {
                        placeViewModel.applyCity(city.toString())
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        placeViewModel.cityLiveData.observe(viewLifecycleOwner) { city ->
            placeViewModel.applyPlaces(city.lat.toString(), city.lon.toString())
        }
        placeViewModel.placesLiveData.observe(viewLifecycleOwner) { place ->
            when (place) {
                is Resource.Success -> {
                    val list =
                        sortedList(place.data?.features!!)
                    placeListAdapter.submitList(list)
                    createChips(list)
                    stopShimmerAnimation()
                }
                is Resource.Loading -> {
                    startShimmerAnimation()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), place.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun sortedList(featuresList: List<Feature>) =
        featuresList.sortedByDescending { it.properties.rate }
            .distinctBy { it.properties.name }

    private fun startShimmerAnimation() {
        fragmentSearchBinding.apply {
            placesRecyclerView.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
        }
    }

    private fun stopShimmerAnimation() {
        fragmentSearchBinding.apply {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            fragmentSearchBinding.placesRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun createChips(list: List<Feature>) {
        val chipList = list.map { it.properties.kinds.split(",") }.flatten().distinct()
        chipList.forEach { category ->
            fragmentSearchBinding.chipGroup.addView(requireContext().createChip(category))
        }
    }

}