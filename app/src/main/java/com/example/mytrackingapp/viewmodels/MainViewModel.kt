package com.example.mytrackingapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytrackingapp.database.Track
import com.example.mytrackingapp.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
//val repository: Repository
): ViewModel() {
    fun insertTrack(track: Track) {

    }
}
    /*private val tracksSortedByDate = Repository.getAllTracksSortedByDate()
     private val tracksSortedByDistance = Repository.getAllTracksSortedByDistance()
     private val tracksSortedByTime = Repository.getAllTracksSortedByTime()
     private val tracksSortedByAvgSpeed = Repository.getAllTracksSortedByAvgSpeed()

     val tracks = MediatorLiveData<List<Track>>()
     var sortType = SortType.DATE

     init {
         tracks.addSource(tracksSortedByDate) { result ->
             if (sortType == SortType.DATE) {
                 result?.let { tracks.value = it }
             }
         }

     }


     fun sorTracks(sortType: SortType) = when(sortType) {
         SortType.DATE -> tracksSortedByDate.value?.let { tracks.value = it }

     }.also {
         this.sortType = sortType
     }

     fun insertTrack(track: Track) = viewModelScope.launch {
         repository.insertTrack(track)
     }

     */




