
package com.example.mytrackingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
  entities = [Track::class],
  version = 1, exportSchema = false
)

@TypeConverters(Converters::class)
abstract class TrackingDataBase : RoomDatabase(){

    abstract fun getTrackDao(): TrackDao

    companion object{
        var INSTANCE: TrackingDataBase? = null
    }

    fun getTrackingDatabase(context: Context): TrackingDataBase?{
        if (INSTANCE==null){
            synchronized(TrackingDataBase::class){
                INSTANCE = Room.databaseBuilder(context.applicationContext, TrackingDataBase::class.java, "myDb").build()

            }
        }
        return INSTANCE
    }
}
