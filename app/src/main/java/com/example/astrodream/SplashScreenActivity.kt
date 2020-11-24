package com.example.astrodream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            callInitialActivity()
            finish()
        }, 3000)
    }

    fun callInitialActivity() {
        val intent = Intent(this, GlobeActivity::class.java)
        startActivity(intent)
    }
}