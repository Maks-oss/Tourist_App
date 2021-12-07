package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vkpi.touristapp.R
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.databinding.FragmentDetailedPlaceBinding
import com.vkpi.touristapp.databinding.FragmentSearchBinding
import com.vkpi.touristapp.utils.IMAGE_NOT_FOUND_URL
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import com.vkpi.touristapp.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedPlaceFragment : Fragment() {
    private lateinit var fragmentDetailedPlaceBinding: FragmentDetailedPlaceBinding
    private val placeViewModel by viewModels<PlaceViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private val args: DetailedPlaceFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDetailedPlaceBinding =
            FragmentDetailedPlaceBinding.inflate(inflater, container, false)
        return fragmentDetailedPlaceBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.placeId?.let {
            if (findNavController().previousBackStackEntry!!.destination.id == R.id.savedFragment)
                placeViewModel.applyPlaceDetailFromDatabase(it)
            else
                placeViewModel.applyPlaceDetailFromServer(
                    it,
                    userViewModel.userLiveData.value!!.userId
                )
        }
        applySaveButton()
        fragmentDetailedPlaceBinding.savePlace.setOnClickListener {
            placeViewModel.insertPlaceIntoDb(
                placeViewModel.placeDetailLiveData.value!!
            )
        }
        setupObserver()
    }

    private fun applySaveButton() {
        fragmentDetailedPlaceBinding.savePlace.visibility = if (findNavController().previousBackStackEntry!!.destination.id == R.id.savedFragment) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun setupObserver() {
        placeViewModel.placeDetailLiveData.observe(viewLifecycleOwner) {
            Glide.with(fragmentDetailedPlaceBinding.root).load(it.placeImageUrl).transition(
                DrawableTransitionOptions.withCrossFade()
            ).into(fragmentDetailedPlaceBinding.placeImage)
            fragmentDetailedPlaceBinding.placeName.text = it.placeName
            fragmentDetailedPlaceBinding.placeDescription.text = it.placeDescription
        }
    }
}