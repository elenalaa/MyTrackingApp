/*package com.example.mytrackingapp.repository

import com.example.mytrackingapp.database.TrackDao
import com.example.mytrackingapp.model.Track
import javax.inject.Inject

//Collect Data for app
class Repository @Inject constructor(
    val trackDao: TrackDao
) {
    suspend fun insertTrack(track: Track) = trackDao.insertTrack(track)

    suspend fun deleteTrack(track: Track) = trackDao.deleteTrack(track)

  fun getAllTracksSortedByDate() = trackDao.getAllTracksSortedByDate()
    fun getAllTracksSortedByTime() = trackDao.getAllTracksSortedByTime()
    fun getAllTracksSortedByAvgSpeed() = trackDao.getAllTracksSortedByAvgSpeed()
    fun getAllTracksSortedByDistance() = trackDao.getAllTracksSortedByDistance()

    //Functions for statistic fragment
    fun getTotalAvgSpeed() = trackDao.getTolatAvgSpeed()
    fun getTotalDistance() = trackDao.getTolatDistance()
    fun getTotalTime() = trackDao.getTolatTime()*/

