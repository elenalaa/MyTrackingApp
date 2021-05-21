package com.example.mytrackingapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
  entities = [Track::class],
  version = 1, exportSchema = false
)

@TypeConverters(Converters::class)
abstract class TrackingDataBase : RoomDatabase(){

    abstract fun getTrackDao(): TrackDao
}