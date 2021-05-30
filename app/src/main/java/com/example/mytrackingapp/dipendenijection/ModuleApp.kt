package com.example.mytrackingapp.dipendenijection

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.mytrackingapp.MainActivity
import com.example.mytrackingapp.R
import com.example.mytrackingapp.database.TrackingDataBase
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_NAVIGATION_TO_MAP_TRACKING
import com.example.mytrackingapp.moredbclasses.Constants.NOTIFICATION_CHANNEL_ID
import com.example.mytrackingapp.moredbclasses.Constants.PENDING_INTENT_REQUEST_CODE
import com.example.mytrackingapp.moredbclasses.Constants.TRACKING_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton


//For using Dagger need put 2 annotations
@Module
@InstallIn(ServiceComponent::class)
object ModuleApp {

    @ServiceScoped
    @Provides
    fun providePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent {
        return PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            Intent(context, MainActivity::class.java ).apply {
                this.action= ACTION_NAVIGATION_TO_MAP_TRACKING
            },
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_smallrun)
            .setContentIntent(pendingIntent)

    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


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


