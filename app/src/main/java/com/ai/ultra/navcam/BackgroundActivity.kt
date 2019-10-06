package com.ai.ultra.navcam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_background.*

class BackgroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)
    }
    public fun startService(v:View){
        var input=edit_text_input.text.toString()
        var serviceIntent=Intent(this, MyBackgroundService::class.java)
        serviceIntent.putExtra("inputExtra",input)
        startService(serviceIntent)

    }
    public fun stopService(v:View){
        var serviceIntent=Intent(this, MyBackgroundService::class.java)
        stopService(serviceIntent)

    }
   /* override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                Toast.makeText(this, "KeyUp", Toast.LENGTH_SHORT).show()
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                Toast.makeText(this, "KeyDown", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }*/
}
