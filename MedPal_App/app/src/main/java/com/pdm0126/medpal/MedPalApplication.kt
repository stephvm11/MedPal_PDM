package com.pdm0126.medpal

import android.app.Application
import com.pdm0126.medpal.data.AppProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedPalApplication : Application(){

    val appProvider by lazy { AppProvider(this) }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            appProvider.loadSavedSession()
        }
    }

}