package com.example.astrodream.ui.asteroids

import android.annotation.SuppressLint
import android.view.View
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

class AsteroidsFragmentViewModel(val fragment: Fragment) : ViewModel() {
    private val listRequestBuilder = ArrayList<RequestBuilder<GifDrawable>>()

    fun execute(v: View) = viewModelScope.launch {
        onPreExecute(v)
        doInBackground()
        onPostExecute(v)
    }

    @SuppressLint("LongLogTag")
    private suspend fun doInBackground() = withContext(Dispatchers.IO) {
        this.launch {
                listRequestBuilder.addAll(
                    arrayListOf(
                        loadImage(fragment.context?.resources?.getString(R.string.url_imagem_globo)),
                        loadImage(fragment.context?.resources?.getString(R.string.url_esquerda_cima)),
                        loadImage(fragment.context?.resources?.getString(R.string.url_direita_cima)),
                        loadImage(fragment.context?.resources?.getString(R.string.url_esquerda_baixo)),
                        loadImage(fragment.context?.resources?.getString(R.string.url_direta_baixo))
                    )
                )
        }
    }

    private fun onPreExecute(v: View) {
        v.findViewById<ProgressBar>(R.id.progressbar_fragment_asteroides).visibility =
            ProgressBar.VISIBLE
    }

    private fun onPostExecute(v: View) {
        listRequestBuilder[0].into(v.findViewById(R.id.iv_terra))
        listRequestBuilder[1].into(v.findViewById(R.id.iv_asteroids2))
        listRequestBuilder[2].into(v.findViewById(R.id.iv_asteroids1))
        listRequestBuilder[3].into(v.findViewById(R.id.iv_asteroids3))
        listRequestBuilder[4].into(v.findViewById(R.id.iv_asteroids4))
        v.findViewById<ProgressBar>(R.id.progressbar_fragment_asteroides).visibility =
            ProgressBar.GONE
    }

    private fun loadImage(url: String?) =
        Glide.with(fragment)
            .asGif()
            .load(url)
}