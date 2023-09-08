package com.example.ad_demo.ui.view

import android.app.Application
import android.util.Log
import com.example.ad_demo.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            // Android context
            androidContext(this@MainApplication)
            // koin modules
            val module = AppModule()
            val list = listOf(module.vmModule, module.repoModule, module.appModule)
            modules(list)
        }
    }
}