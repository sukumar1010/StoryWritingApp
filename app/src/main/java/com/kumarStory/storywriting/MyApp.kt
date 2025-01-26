package com.kumarStory.storywriting

import android.app.Application
import com.kumarStory.storywriting.BackendFiles.NetworkModule
import com.kumarStory.storywriting.BackendFiles.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
//            startTestScheduler(this@MyApp)
//            scheduleBroadcastReceiver(this@MyApp)
            scheduleMidnightBroadcastReceiver(this@MyApp)
            androidContext(this@MyApp)
            modules(listOf(NetworkModule, ViewModelModule))
        }
    }
}
