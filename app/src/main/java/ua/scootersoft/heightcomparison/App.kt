package ua.scootersoft.heightcomparison

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    companion object {
        lateinit var application: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}