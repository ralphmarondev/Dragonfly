package com.ralphmarondev.dragonfly

import android.app.Application
import com.ralphmarondev.dragonfly.core.common.NotificationHelper
import com.ralphmarondev.dragonfly.core.worker.LocationWorkerScheduler
import com.ralphmarondev.dragonfly.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createChannel(this@MyApp)

        startKoin {
            androidContext(this@MyApp)
            workManagerFactory()
            modules(appModule)
        }

        LocationWorkerScheduler.schedule(this)
    }
}