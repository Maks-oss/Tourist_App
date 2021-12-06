package com.vkpi.touristapp.ui.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vkpi.touristapp.R
import com.vkpi.touristapp.data.Places
import com.vkpi.touristapp.utils.PERMISSION_CODE
import com.vkpi.touristapp.utils.Resource
import com.vkpi.touristapp.utils.createMarker
import com.vkpi.touristapp.utils.setupMap
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private var googleMap: GoogleMap? = null
    private var locationRequest: LocationRequest? = null
    private var lastLocation: Location? = null
    private val placeViewModel by viewModels<PlaceViewModel>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            val locations = p0?.locations
            if (!locations.isNullOrEmpty()) {
                val location = locations.last()
                lastLocation = location
                val latLng = LatLng(location.latitude, location.longitude)
                googleMap?.setupMap(latLng, getString(R.string.current_location))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        setupObserver()
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            placeViewModel.applyPlaces(
                lastLocation!!.latitude.toString(),
                lastLocation!!.longitude.toString()
            )
        }

    }

    private fun setupObserver() {
        placeViewModel.placesLiveData.observe(viewLifecycleOwner) { place ->
            if (place is Resource.Success && place.data != null) {
                place.data.features.filter { it.properties.name.isNotEmpty() }.forEach {
                    googleMap!!.createMarker(
                        LatLng(it.geometry.coordinates[1], it.geometry.coordinates.first()),
                        it.properties.name,
                    ) { marker -> processMarkerClick(marker) }
                }
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lastLocation!!.latitude,
                            lastLocation!!.longitude
                        ), place.data.features.size.toFloat()
                    )
                )

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationProvider?.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )

                    googleMap?.isMyLocationEnabled = true
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        locationRequest = LocationRequest().apply {
            interval = 120000
            fastestInterval = 120000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        checkPermission()
        googleMap?.setOnMapClickListener { latLng ->
            googleMap?.setupMap(
                latLng, getString(
                    R.string.coordinates,
                    latLng.longitude.toString(),
                    latLng.latitude.toString()
                )
            )
            lastLocation =
                Location("").also { it.latitude = latLng.latitude;it.longitude = latLng.longitude }

        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
            googleMap?.isMyLocationEnabled = true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_CODE
            )
        }
    }

    private fun processMarkerClick(marker: Marker): Boolean {
        val latLng = LatLng(
            lastLocation!!.latitude,
            lastLocation!!.longitude
        )
        val id = placeViewModel.getPlaceIdByLocation(marker!!.position)
        return if (marker.position != latLng && !id.isNullOrEmpty()) {
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToDetailedPlaceFragment(
                    id
                )
            )
            true
        } else {
            false
        }
    }

}