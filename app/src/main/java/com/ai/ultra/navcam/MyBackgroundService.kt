package com.ai.ultra.navcam


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.KeyEvent
import android.widget.Toast
import android.support.v4.app.NotificationCompat
import android.app.PendingIntent
import android.content.ComponentName
import android.support.v4.media.VolumeProviderCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.media.MediaPlayer
import android.content.ContentValues.TAG
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import android.content.IntentFilter




class MyBackgroundService : Service() {
    private var mediaSession: MediaSessionCompat? = null
    private var mediaPlayer: MediaPlayer? = null
    var volumePrev = 0
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if ("android.media.VOLUME_CHANGED_ACTION" == intent.action) {

                val volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0)

                Log.i(TAG, "volume = $volume")

                if (volumePrev < volume) {
                    Log.i(TAG, "You have pressed volume up button")
                } else {
                    Log.i(TAG, "You have pressed volume down button")
                }
                volumePrev = volume
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
       // MediaFun()

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        ForegroundFun(intent)
        //mediaPlayer = MediaPlayer.create(this, R.raw.blank)
       // mediaPlayer!!.setLooping(true)
       // mediaPlayer!!.start()
        var filter = IntentFilter()
        filter.addAction("android.media.VOLUME_CHANGED_ACTION")
        registerReceiver(broadcastReceiver, filter)
        return START_STICKY
    }

    fun ForegroundFun(intent: Intent){
        var input=intent.getStringExtra("inputExtra")

        var notificationIntent=Intent(this,BackgroundActivity::class.java)
        var pendingIntent=PendingIntent.getActivity(this,
                0,notificationIntent,0)

        var notification =NotificationCompat.Builder(this,App.CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_camera_enhance)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1,notification)
        Toast.makeText(this, "background", Toast.LENGTH_SHORT).show()
    }

    fun MediaFun(){
        mediaSession = MediaSessionCompat(this, "PlayerService")
        mediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession!!.setPlaybackState(PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0f) //you simulate a player which plays something.
                .build())

        //this will only work on Lollipop and up, see https://code.google.com/p/android/issues/detail?id=224134
        val myVolumeProvider = object : VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE, /*max volume*/100, /*initial volume level*/50) {
            override fun onAdjustVolume(direction: Int) {
                /*
                -1 -- volume down
                1 -- volume up
                0 -- volume button released
                 */
                when (direction){
                    -1 -> Toast.makeText(this@MyBackgroundService, "down", Toast.LENGTH_LONG).show()
                    1 -> Toast.makeText(this@MyBackgroundService, "up", Toast.LENGTH_LONG).show()
                }
            }
        }

        mediaSession!!.setPlaybackToRemote(myVolumeProvider)
        mediaSession!!.setActive(true)
    }

    override fun onDestroy() {
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
       // mediaSession!!.release()
    }
}
