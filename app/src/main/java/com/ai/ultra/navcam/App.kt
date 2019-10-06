package com.ai.ultra.navcam

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App:Application() {
    companion object {
        public val CHANNEL_ID="ForegroundServiceChannel"
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var FserviceChannel=NotificationChannel(CHANNEL_ID,"Foreground Service Channel",NotificationManager.IMPORTANCE_DEFAULT)
            var manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(FserviceChannel)

        }

    }
}