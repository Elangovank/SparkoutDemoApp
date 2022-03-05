package com.elango.demoapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elango.demoapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_map_details.*

class MapDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mapFragment: SupportMapFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_details)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        intent.extras?.let {
            val latLng =
                LatLng(it.getString("lat")?.toDouble()!!, it.getString("lng")?.toDouble()!!)
            p0.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            p0.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            addresstext.text = it.getString("address")

        }

    }
}