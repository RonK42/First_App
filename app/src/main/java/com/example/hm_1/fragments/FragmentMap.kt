package com.example.hm_1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.hm_1.R

class FragmentMap : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble("latitude", 0.0)
            longitude = it.getDouble("longitude", 0.0)
        }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            Log.e("FragmentMap", "SupportMapFragment is null. Check if it is properly initialized in XML.")
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(location).title("Player Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    companion object {
        fun newInstance(latitude: Double, longitude: Double): FragmentMap {
            val fragment = FragmentMap()
            val bundle = Bundle().apply {
                putDouble("latitude", latitude)
                putDouble("longitude", longitude)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
