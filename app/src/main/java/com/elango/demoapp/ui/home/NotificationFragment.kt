package com.elango.demoapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.elango.demoapp.R
import com.elango.demoapp.model.MapDataDAO
import com.elango.demoapp.model.MapModel
import com.elango.demoapp.util.LocationLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notification.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    val REQUEST_CODE = 101
    lateinit var data: MapModel

    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Test Notification"

    @Inject
    @Named("LocationDAO")
    lateinit var mMapDao: MapDataDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = mMapDao.getMapDetails()
        if (!list.isNullOrEmpty() && list.size > 0) {
            data = list.get(list.size-1)
            fetchLocation()
        } else {
            statusTxt.text = getString(R.string.txt_location_not_saved)
        }
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
            if (this::data.isInitialized) {

                val startPoint = Location(getString(R.string.txt_source))
                startPoint.latitude = data.lat.toDouble()
                startPoint.longitude = data.lng.toDouble()

                val endPoint = location

                val distance = startPoint.distanceTo(endPoint).toDouble()

                if (distance > 3000) {
                    statusTxt.text = getString(R.string.txt_away)
                    triggerNotification(getString(R.string.txt_away))
                } else {
                    statusTxt.text = getString(R.string.txt_inside)
                    triggerNotification(getString(R.string.txt_inside))
                }
            }
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    fun triggerNotification(msg: String) {
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            Intent(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(requireContext(), channelId).setContentTitle("Demo App")
                .setContentText(msg).setSmallIcon(R.drawable.ic_app).setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources, R.drawable
                            .ic_launcher_background
                    )
                ).setContentIntent(pendingIntent)
        }
        notificationManager.notify(12345, builder.build())
    }


}