package com.ai.ultra.navcam

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorMatrix
import android.view.KeyEvent
import android.widget.Toast
import android.media.AudioManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.util.jar.Manifest
import android.content.DialogInterface
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.support.v7.app.AlertDialog


class MainActivity : AppCompatActivity() {
    var camera_permission_code=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        }
        open_camera_btn.setOnClickListener {
         startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),222)
        }
        backgroundBtn.setOnClickListener {
            var backgIntent=Intent(this,BackgroundActivity::class.java)
            startActivity(backgIntent)
        }
        videoBtn.setOnClickListener {
            var videoIntent=Intent(this,VideoActivity::class.java)
            startActivity(videoIntent)
        }
    }
    fun requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA)){
            AlertDialog.Builder(this)
                    .setTitle("Camera Permission needed")
                    .setMessage("the app is useless without this permission")
                    .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(android.Manifest.permission.CAMERA), camera_permission_code)
                    })
                    .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                    .create().show()
        }
        else{
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA), camera_permission_code)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == camera_permission_code){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED)
            {Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==222){
            var bmp=data.extras.get("data") as Bitmap
            var image=blackwhiteimage(bmp)
            imageView1.setImageBitmap(image)

        }
    }
    fun blackwhiteimage(orginal:Bitmap):Bitmap{
        var result:Bitmap= Bitmap.createBitmap(orginal.width,orginal.height,orginal.config)
        var colorpixel:Int?=null
        var width=orginal.width
        var height=orginal.height
        var A:Int?=null
        var R:Int?=null
        var G:Int?=null
        var B:Int?=null
        for (x in 0..(width-1)){
            for (y in 0..(height-1)){
                colorpixel=orginal.getPixel(x,y)
                A=Color.alpha(colorpixel)
                R=Color.red(colorpixel)
                G=Color.green(colorpixel)
                B=Color.blue(colorpixel)
                R=(R*0.3).toInt()+(G*0.59).toInt()+(B*0.11).toInt()
                G=R
                B=R
                result.setPixel(x,y,Color.argb(A,R,G,B))
            }
        }
        return result
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
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
    }
}
