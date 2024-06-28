package com.lee_idle.soribada

import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    init{

    }

    fun GoToHome(){
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        SoriBadaApplication.ApplicationContext().startActivity(intent)
    }
}