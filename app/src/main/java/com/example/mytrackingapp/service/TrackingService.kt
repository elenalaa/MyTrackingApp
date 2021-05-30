

package com.example.mytrackingapp.service

//import com.example.mytrackingapp.moredbclasses.TrackingPermissions
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_STOP_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_CHANNEL_ID
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_CHANNEL_NAME
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    /* var firstTrack = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val time = MutableLiveData<Long>()*/

    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notification: NotificationCompat.Builder


    companion object {
        val started = MutableLiveData<Boolean>()
        val time = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val trackPoints = MutableLiveData<MutableList<LatLng>>()
    }

    private fun postInitialValues() {
        started.postValue(false)
        isTracking.postValue(false)
        trackPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        postInitialValues()
        super.onCreate()
    }
    /*  fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }*/

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    started.postValue(true)
                }
                ACTION_STOP_SERVICE -> started.postValue(false)
                else -> {
                }
            }
        }
            return super.onStartCommand(intent, flags, startId)
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

}


/*
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
}*/

