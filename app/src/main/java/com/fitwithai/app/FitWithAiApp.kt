package com.fitwithai.app

import android.app.Application
import com.fitwithai.di.koinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FitWithAiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppInitializer.init(this)
        startKoin {
            androidContext(this@FitWithAiApp)
            modules(koinModules)
        }
    }
}
