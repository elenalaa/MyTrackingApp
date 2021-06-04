package com.example.mytrackingapp.ui.fragments

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

object MapSet {
    //Need this function for follow User location
    fun cameraPosition(location: LatLng): CameraPosition {
        return CameraPosition.Builder()
            .target(location)
            .zoom(18f)
            .build()
    }

    fun updatedTimeForm(startTime: Long, stopTime: Long): String {
        val updatedTime = stopTime - startTime

        val seconds = (updatedTime / 1000).toInt() % 60
        val minutes = (updatedTime / (1000 * 60) % 60)
        val hours = (updatedTime / (1000 * 60 * 60) % 24)

        return "$hours:$minutes:$seconds"
    }

    //Use implementation lab Util for google map
    fun calculateDistance(locationList: MutableList<LatLng>): String {

        //Check is LocationList empty or not and if it is not, calculate distance between start
        //and stop points
        if (locationList.size > 1) {
            val meters =
                SphericalUtil.computeDistanceBetween(locationList.first(), locationList.last())
            val kilometers = meters / 1000
            return DecimalFormat("#.##").format(kilometers)
        }
        return "00.00"
    }

    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(!includeMillis) {
            return "${if(hours < 10) "0" else ""}$hours:" +
                    "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds:" +
                "${if(milliseconds < 10) "0" else ""}$milliseconds"
    }
}

