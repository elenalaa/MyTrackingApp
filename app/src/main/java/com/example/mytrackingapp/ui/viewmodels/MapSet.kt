package com.example.mytrackingapp.ui.viewmodels

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

object MapSet {

    fun cameraPosition(location: LatLng): CameraPosition {
        return CameraPosition.Builder()
            .target(location)
            .zoom(18f)
            .build()
    }

    fun updatedTimeForm(startTime: Long, stopTime: Long): String{
        val updatedTime = stopTime-startTime

        val seconds = (updatedTime/1000).toInt() % 60
        val minutes = (updatedTime/(1000 * 60) % 60)
        val hours = (updatedTime / (1000 *60*60) % 24)

        return "$hours:$minutes:$seconds"
    }
}
