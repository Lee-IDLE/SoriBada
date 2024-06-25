package com.lee_idle.soribada

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.view.WindowCompat
import androidx.lifecycle.MutableLiveData

class SoriBadaApplication : Application() {
    companion object {
        lateinit var instance: SoriBadaApplication
        var darkTheme = MutableLiveData<Boolean>(true)

        fun ApplicationContext(): Context {
            return instance.applicationContext
        }
    }

    val db by lazy {

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        val sharedPreferences: SharedPreferences =
            instance.getSharedPreferences("sori_bada_app", Context.MODE_PRIVATE)
        darkTheme.value = sharedPreferences.getBoolean("theme", true)
    }
}