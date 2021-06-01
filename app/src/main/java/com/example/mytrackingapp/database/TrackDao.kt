package com.example.mytrackingapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.mytrackingapp.model.Track


@Dao
interface TrackDao {
    //if some problem with single track, it will be updated with new one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)
    //Delete Track
    @Delete
    suspend fun deleteTrack(track: Track)
    //Track sorted by date
    /*@Query("SELECT * FROM tracking_table ORDER BY timestamp DESC")
    fun getAllTracksSortedByDate(): LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY time DESC")
    fun getAllTracksSortedByTime(): LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY avgSpeedKMH DESC")
    fun getAllTracksSortedByAvgSpeed(): LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY distanceMeters DESC")
    fun getAllTracksSortedByDistance(): LiveData<List<Track>>


    //All Total parameters from tracking table
    @Query("SELECT SUM(time) FROM tracking_table")
    fun getTolatTime(): LiveData<Long>

    @Query("SELECT SUM(distanceMeters) FROM tracking_table")
    fun getTolatDistance(): LiveData<Int>

    @Query("SELECT SUM(avgSpeedKMH) FROM tracking_table")
    fun getTolatAvgSpeed(): LiveData<Float>*/
}
