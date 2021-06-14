package com.example.mytrackingapp.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TrackDao {
    //if some problem with single track, it will be updated with new one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)

    //Delete Track
    @Delete
    suspend fun deleteTrack(track: Track)
    //Track sorted by date
    @Query("SELECT * FROM tracking_table ORDER BY time DESC")
    fun getAllTracksSortedByDate(): LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY time DESC")
    fun getAllTracksSortedByTime(): LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY avgSpeedKMH DESC")
    fun getAllTracksSortedByAvgSpeed(): LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY distance DESC")
    fun getAllTracksSortedByDistance(): LiveData<List<Track>>


    //All Total parameters from tracking table
    @Query("SELECT SUM(time) FROM tracking_table")
    fun getTolatTime(): LiveData<Long>

    @Query("SELECT SUM(distance) FROM tracking_table")
    fun getTolatDistance(): LiveData<Int>

    @Query("SELECT SUM(avgSpeedKMH) FROM tracking_table")
    fun getTolatAvgSpeed(): LiveData<Float>
}
