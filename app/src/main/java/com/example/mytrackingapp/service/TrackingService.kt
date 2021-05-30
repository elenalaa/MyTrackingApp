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
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_STOP_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.FASTEST_LOCATION_INTERVAL
import com.example.mytrackingapp.moredbclasses.Constants.LOCATION_UPDATE_INTERVAL
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_CHANNEL_ID
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_ID
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    //var firstTrack = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //private val time = MutableLiveData<Long>()

    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notification: NotificationCompat.Builder


    companion object {
        val started = MutableLiveData<Boolean>()
        var startTime = MutableLiveData<Long>()
        var stopTime = MutableLiveData<Long>()
        val trackPointsList = MutableLiveData<MutableList<LatLng>>()
    }

    private fun postInitialValues() {
        started.postValue(false)
        trackPointsList.postValue(mutableListOf())
        startTime.postValue(0L)
        stopTime.postValue(0L)
    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result?.locations?.let { locations ->
                for (location in locations) {
                    updateTrackPointList(location)

                }
            }
        }
    }

    private fun updateTrackPointList(location: Location?) {
        val newLatLng = LatLng(location!!.latitude, location.longitude)
        trackPointsList.value?.apply {
            add(newLatLng)
            trackPointsList.postValue(this)
        }
   }


    override fun onCreate() {
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate()
        /*isTracking.observe(this, Observer {
            updateLocationTracking(it)*/
    }


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
                else -> {
                }
            }
        }
            return super.onStartCommand(intent, flags, startId)
        }

    private fun startForegroundService(){
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun stopForegroundService() {
        removeLocationUpdates()
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
        stopForeground(true)
        stopSelf()
        startTime.postValue(System.currentTimeMillis())
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


/*private fun pauseService() {
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
    }*/

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = FASTEST_LOCATION_INTERVAL
            priority = PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallBack,
                    Looper.getMainLooper()
        )
        startTime.postValue(System.currentTimeMillis())
    }
}




