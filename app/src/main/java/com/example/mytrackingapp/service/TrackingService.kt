package com.example.mytrackingapp.service

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_PAUSE_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_STOP_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.FASTEST_LOCATION_INTERVAL
import com.example.mytrackingapp.moredbclasses.Constants.LOCATION_UPDATE_INTERVAL
import com.example.mytrackingapp.moredbclasses.TrackingPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng

class TrackingService : LifecycleService() {


    var firstTrack = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val time = MutableLiveData<Long>()

    companion object {
        val time = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val trackPoints = MutableLiveData<MutableList<LatLng>>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        trackPoints.postValue(mutableListOf())
    }

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {

                }
                ACTION_PAUSE_SERVICE -> {

                }
                ACTION_STOP_SERVICE -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var timeEnable = false
    //Time for pause
    private var lapTime = 0L
    //Final time of track
    private var timeTrack = 0L

    val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations.let { location ->
                    for (location in location) {
                        addTrackPoint(location)

                    }
                }
            }
        }
    }

    private fun addTrackPoint(location: Location?) {
        location?.let {
            val position = LatLng(location.latitude, location.longitude)
        }
    }


    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingPermissions.checkLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallBack,
                    Looper.getMainLooper()
                )
            } else {
                fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
            }
        }
    }
}