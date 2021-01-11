package com.example.astrodream.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.activity_full_screen_img.*
import kotlin.math.max
import kotlin.math.min

class FullScreenImgActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_img)

        val img = intent.extras?.getString("img")
        Glide.with(this).asBitmap()
            .load(img)
            .into(ivFull)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        ic_close_fullscreen.setOnClickListener { finish() }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(1.0f, min(scaleFactor, 10.0f))
            ivFull.scaleX = scaleFactor
            ivFull.scaleY = scaleFactor
            return true
        }
    }
}