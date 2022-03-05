package com.elango.demoapp.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.elango.demoapp.R
import com.elango.demoapp.model.MapDataDAO
import com.elango.demoapp.model.MapModel
import com.elango.demoapp.util.LocationLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {
    @Inject
    @Named("LocationDAO")
    lateinit var mLocationDao: MapDataDAO

    var mContext: Context? = null
    var mLocationMarkerText: TextView? = null
    var addresstext: TextView? = null
    private var mCenterLatLong: LatLng? = null
    var mLocationAddress: EditText? = null
    var mLocationText: TextView? = null

    lateinit var mapFragment: SupportMapFragment
    var currentLocation: Location? = null

    val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mLocationMarkerText = view.findViewById(R.id.locationMarkertext)
        addresstext = view.findViewById(R.id.addresstext)
        mLocationAddress = view.findViewById(R.id.Address)
        mLocationText = view.findViewById(R.id.Locality)

        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }

        val aLocationLiveData = LocationLiveData(activity)
        aLocationLiveData.observe(viewLifecycleOwner) { location: Location ->
            currentLocation = location
            setCurrentLocation()
        }
        mapFragment.getMapAsync(this)
    }

    fun setCurrentLocation() {
        currentLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

        }
    }

    override fun onMapReady(aMap: GoogleMap) {
        mMap = aMap
        setCurrentLocation()

        mMap.setOnCameraIdleListener(object : GoogleMap.OnCameraIdleListener {
            override fun onCameraIdle() {

                mCenterLatLong = mMap.cameraPosition.target
                mMap.clear()
                try {
                    val mLocation = Location("")
                    mLocation.latitude = mCenterLatLong!!.latitude
                    mLocation.longitude = mCenterLatLong!!.longitude
                    mLocationMarkerText!!.text =
                        "Lat : ${mCenterLatLong!!.latitude} Long : ${mCenterLatLong!!.longitude}"
                    val address = convertLatLngtoAddress(mLocation.latitude, mLocation.longitude)
                    address?.let {
                        addresstext!!.text = it
                    }

                    save!!.setOnClickListener {
                        val data = MapModel(
                            mobile = FirebaseAuth.getInstance().currentUser?.phoneNumber!!,
                            lat = mCenterLatLong!!.latitude.toString(),
                            lng = mCenterLatLong!!.longitude.toString(),
                            address = address!!
                        )

                        mLocationDao.insert(data)

                        Log.e("Data", mLocationDao.getMapDetails().toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })


    }


    fun convertLatLngtoAddress(latitude: Double, longitude: Double): String? {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(mContext, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        )

        if (addresses.size > 0) {
            val address: String =
                addresses[0].getAddressLine(0)
            return address
        } else {
            return null
        }
    }
}