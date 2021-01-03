package com.example.astrodream.ui.asteroids

import android.content.res.Resources
import android.util.AndroidRuntimeException
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.example.astrodream.R
import com.example.astrodream.domain.exceptions.InternetConnectionException
import com.example.astrodream.domain.exceptions.UnknownErrorException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.Exception

class AsteroidsFragmentViewModel(val fragment: Fragment): ViewModel() {
    private val listRequestBuilder = ArrayList<RequestBuilder<GifDrawable>>()

    fun execute(v: View) = viewModelScope.launch {
        onPreExecute(v)
        var result = ""
        try { result = doInBackground(v) }
        catch (e: InternetConnectionException){ fragment.context?.let { e.showImageWithoutInternetConnection(it) } }
        catch (e: UnknownErrorException){ fragment.context?.let { e.showImageUnknownError(it) }}
        onPostExecute(result, v)
    }

    private suspend fun doInBackground(v: View): String = withContext(Dispatchers.IO) {
            launch {
                try {
                listRequestBuilder.addAll(arrayListOf(loadImage(Resources.getSystem().getString(R.string.url_imagem_globo)),
                    loadImage(Resources.getSystem().getString(R.string.url_esquerda_cima)),
                    loadImage(Resources.getSystem().getString(R.string.url_direita_cima)),
                    loadImage(Resources.getSystem().getString(R.string.url_esquerda_baixo)),
                    loadImage(Resources.getSystem().getString(R.string.url_direta_baixo))))
                } catch (e: IOException) { throw InternetConnectionException() }
                  catch (e: Exception) { throw UnknownErrorException() }
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