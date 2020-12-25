package com.example.astrodream.ui.asteroids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import com.example.astrodream.R

class AsteroidOrbitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid_orbit)

      //  val url = intent.getStringExtra("url_asteroid_orbit")
        //if (url != null) webView_asteroids.loadUrl(url)
//        val webView: WebView = findViewById(R.id.webView_asteroids)
//        webView.loadUrl("https://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2465633;orb=1;cov=0;log=0;cad=0#orb")

        val webView = WebView(this)
        setContentView(webView)
        webView.loadUrl("https://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2465633;orb=1;cov=0;log=0;cad=0#orb")

        Log.i("ASTEROID ORBIT ACTIVTY", "on create")
    }
}
