package com.example.mytrackingapp.model


import android.os.Parcelable

import kotlinx.parcelize.Parcelize

//@Entity(tableName= "tracking_table")
@Parcelize
data class Track (

    //var avgSpeedKMH: Float = 0f,
    var distance: String,
    var time: String


) : Parcelable
