package com.example.mytrackingapp.dipendenijection

import android.content.Context
import androidx.room.Room
import com.example.mytrackingapp.database.TrackingDataBase
import com.example.mytrackingapp.moredbclasses.Constants.TRACKING_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

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


