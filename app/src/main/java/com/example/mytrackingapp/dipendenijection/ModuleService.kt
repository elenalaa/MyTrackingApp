package com.example.mytrackingapp.dipendenijection

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mytrackingapp.MainActivity
import com.example.mytrackingapp.R
import com.example.mytrackingapp.moredbclasses.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

object ModuleService {

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
                Constants.PENDING_INTENT_REQUEST_CODE,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        @ServiceScoped
        @Provides
        fun provideNotificationBuilder(
            @ApplicationContext context: Context,
            pendingIntent: PendingIntent
        ): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
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
    }
}