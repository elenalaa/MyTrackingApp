package com.example.mytrackingapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName= "tracking_table")
//@Parcelize
data class Track(
    var timestamp: Long = 0L,
    var avgSpeedKMH: Float = 0f,
    var distance: Int = 0,
    var time: Long = 0L

){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
//) : Parcelable
