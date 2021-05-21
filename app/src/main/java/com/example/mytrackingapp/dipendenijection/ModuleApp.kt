package com.example.mytrackingapp.dipendenijection

import android.content.Context
import androidx.room.Room
import com.example.mytrackingapp.database.TrackingDataBase
import com.example.mytrackingapp.moredbclasses.Constants.TRACKING_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//For using Dagger need put 2 annotations
@Module
@InstallIn(SingletonComponent::class)
object ModuleApp {

    @Singleton
    @Provides
    fun provideTrackingDataBase(
        @ApplicationContext app:Context
    ) = Room.databaseBuilder(
        app,
        TrackingDataBase::class.java,
        TRACKING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides

    fun provideTrackDao(db: TrackingDataBase) = db.getTrackDao()
}


