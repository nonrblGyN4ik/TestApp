package com.app.testapp

import android.app.Application
import com.app.testapp.utils.AppModule

class AAApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppModule.init(this)
    }
}