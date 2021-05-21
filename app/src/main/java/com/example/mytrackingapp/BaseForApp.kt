package com.example.mytrackingapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


//Use Dagger Hilt
@HiltAndroidApp

class BaseForApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}