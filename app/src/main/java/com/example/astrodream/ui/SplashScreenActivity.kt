package com.example.astrodream.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.astrodream.R
import com.example.astrodream.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            callInitialActivity()
            finish()
        }, 1000)
    }

    private fun callInitialActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}