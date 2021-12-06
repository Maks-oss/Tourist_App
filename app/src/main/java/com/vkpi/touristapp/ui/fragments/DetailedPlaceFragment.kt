package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vkpi.touristapp.R
import com.vkpi.touristapp.databinding.FragmentDetailedPlaceBinding
import com.vkpi.touristapp.databinding.FragmentSearchBinding
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedPlaceFragment: Fragment() {
    private lateinit var fragmentDetailedPlaceBinding:FragmentDetailedPlaceBinding
    private val placeViewModel by viewModels<PlaceViewModel>()
    val args: DetailedPlaceFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDetailedPlaceBinding = FragmentDetailedPlaceBinding.inflate(inflater, container, false)
        return fragmentDetailedPlaceBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.placeId?.let {
            placeViewModel.applyPlaceDetail(it)
        }
        setupObserver()
    }
    private fun setupObserver(){
        placeViewModel.placeDetailLiveData.observe(viewLifecycleOwner){
            Log.d("TAG", "setupObserver: $it")
            Glide.with(fragmentDetailedPlaceBinding.root).load(it.preview.source).into(fragmentDetailedPlaceBinding.placeImage)
            fragmentDetailedPlaceBinding.placeName.text=it.name
            fragmentDetailedPlaceBinding.placeDescription.text=it.wikipedia_extracts.text
        }
    }
}