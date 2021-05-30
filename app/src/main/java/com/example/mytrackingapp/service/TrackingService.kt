
/*
package com.example.mytrackingapp.service

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_PAUSE_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_STOP_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.FASTEST_LOCATION_INTERVAL
import com.example.mytrackingapp.moredbclasses.Constants.LOCATION_UPDATE_INTERVAL
import com.example.mytrackingapp.moredbclasses.Constants.TIMER_UPDATE_INTERVAL
//import com.example.mytrackingapp.moredbclasses.TrackingPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                    if(firstTrack) {
                        firstTrack = false
                    } else{
                        startTimer()
                        Log.println(Log.INFO,"start", "start tracking")
                    }

                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService()

                }
                ACTION_STOP_SERVICE -> {

                }
                else -> Log.println(Log.INFO,"stop", "stop tracking")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }




    private fun pauseService() {
        isTracking.postValue(false)
        timeEnable = false
    }

    private var timeEnable = false
    //Time for pause
    private var lapTime = 0L
    //Final total time of track
    private var timeTrack = 0L
    private var timestamp = 0L
    private var lastMoment = 0L
    private var timeStarted = 0L

    private fun startTimer(){
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        timeEnable = true
        CoroutineScope(Dispatchers.Main!!).launch{
            while(isTracking.value!!) {
                lapTime = System.currentTimeMillis()-timeStarted

                time.postValue(timeTrack+lapTime)

            }
            delay(TIMER_UPDATE_INTERVAL)
        }
        timeTrack += lapTime
    }


private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { location ->
                    for (location in location) {
                        addTrackPoint(location)
                        Log.println(Log.INFO,"novaja location", "${location.latitude}")
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
*/
