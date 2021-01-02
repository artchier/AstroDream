package com.example.astrodream.ui.asteroids

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.example.astrodream.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsteroidsFragmentViewModel(val fragment: Fragment): ViewModel() {
    private val listRequestBuilder = ArrayList<RequestBuilder<GifDrawable>>()

    fun execute(v: View) = viewModelScope.launch {
        onPreExecute(v)
        val result = doInBackground(v)
        onPostExecute(result, v)
    }

    private suspend fun doInBackground(v: View): String = withContext(Dispatchers.IO) {
        launch {
            listRequestBuilder.addAll(arrayListOf(loadImage("https://i.ibb.co/gzJBqz8/planeta.gif"),
                loadImage("https://i.ibb.co/CbVJ2hD/20210102-001705.gif"),
                loadImage("https://i.ibb.co/Jv2rWVw/20210102-002216.gif"),
                loadImage("https://i.ibb.co/Bz5Lytp/20210102-002637.gif"),
                loadImage("https://i.ibb.co/Pc46jmT/20210102-002851.gif")))
        }
        return@withContext "executado"
    }

    private fun onPreExecute(v: View){ v.findViewById<ProgressBar>(R.id.progressbar_fragment_asteroides).visibility = ProgressBar.VISIBLE }

    private fun onPostExecute(result: String, v: View){
        if (result == "executado") {
            listRequestBuilder[0].into(v.findViewById(R.id.iv_terra))
            listRequestBuilder[1].into(v.findViewById(R.id.iv_asteroids2))
            listRequestBuilder[2].into(v.findViewById(R.id.iv_asteroids1))
            listRequestBuilder[3].into(v.findViewById(R.id.iv_asteroids3))
            listRequestBuilder[4].into(v.findViewById(R.id.iv_asteroids4))
            v.findViewById<ProgressBar>(R.id.progressbar_fragment_asteroides).visibility =
                ProgressBar.GONE
        }
    }

    private fun loadImage(url: String) =
        Glide.with(fragment)
            .asGif()
            .load(url)
}