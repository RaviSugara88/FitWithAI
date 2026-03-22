package com.fitwithai.app

import android.app.Application
import com.fitwithai.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FitWithAiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FitWithAiApp)
            modules(appModule)
        }
    }
}
