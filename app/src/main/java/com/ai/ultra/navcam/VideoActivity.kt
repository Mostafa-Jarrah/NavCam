package com.ai.ultra.navcam

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_video.*
import com.ai.ultra.navcam.R.id.videoView



class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        start_video_btn.setOnClickListener {
            var vIntent=Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(vIntent,111)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode==111){
            var bmp:Uri=data.data
            var mediaController=MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(bmp)
            videoView.start()

        }
    }
}
