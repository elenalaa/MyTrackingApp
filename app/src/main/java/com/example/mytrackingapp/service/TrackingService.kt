@file:Suppress("DEPRECATION")

package com.example.mytrackingapp.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mytrackingapp.database.Track
import com.example.mytrackingapp.database.TrackDao
import com.example.mytrackingapp.database.TrackingDataBase
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_STOP_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.FASTEST_LOCATION_INTERVAL
import com.example.mytrackingapp.moredbclasses.Constants.LOCATION_UPDATE_INTERVAL
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_CHANNEL_ID
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_ID
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService(){

    //var isFirstTrack = true
    //var serviceKilled = false
    private var db: TrackingDataBase? = null
    private var trackDao: TrackDao? = null
    //User location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notification: NotificationCompat.Builder


    companion object {
        val started = MutableLiveData<Boolean>()
        var startTime = MutableLiveData<Long>()
        var stopTime = MutableLiveData<Long>()
        val locationList = MutableLiveData<MutableList<LatLng>>()
    }

    private fun postInitialValues() {
        started.postValue(false)
        locationList.postValue(mutableListOf())
        startTime.postValue(0L)
        stopTime.postValue(0L)

    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.let { locations ->
                for (location in locations) {
                    updateLocationList(location)

                }
            }
        }
    }

    private fun updateLocationList(location: Location?) {
        val newLatLng = LatLng(location!!.latitude, location.longitude)
        locationList.value?.apply {
            add(newLatLng)
            locationList.postValue(this)
        }
   }


    override fun onCreate() {
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate()

        started.observe(this, Observer {

        })
    }

    //If action start or stop command -> started Foreground Service, which update location
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    started.postValue(true)
                    startForegroundService()
                    startLocationUpdates()
                }
                ACTION_STOP_SERVICE -> {
                    started.postValue(false)
                    stopForegroundService()
                }
                else -> { }
            }
        }
            return super.onStartCommand(intent, flags, startId)
    }
    //Start Foreground Service
    private fun startForegroundService(){
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notification.build())
    }

    //Stop Foreground Service
    private fun stopForegroundService() {
        removeLocationUpdates()
        //Close notification
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
        stopForeground(true)
        stopSelf()
        Log.d("Stop Foreground Service", locationList.toString())

        stopTime.postValue(System.currentTimeMillis())

        var time = startTime.value!!.minus(startTime.value!!)
        var distance = 10.0F

        var speed = 0L

        var timestamp = 0L
        var track = Track(timestamp, speed.toFloat(), distance.toInt(), time)

        Log.d("track", track.toString())

   /*viewModelScope.launch {
            db = TrackingDataBase.getTrackingDatabase(context = this)
            trackDao = db?.getTrackDao()
            with(trackDao) {
                this?.insertTrack(track)
            }

        }*/
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
    }

    private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    IMPORTANCE_LOW
                )
                notificationManager.createNotificationChannel(channel)
            }
        }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = FASTEST_LOCATION_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallBack,
                    Looper.getMainLooper()
        )
        startTime.postValue(System.currentTimeMillis())
    }
}




