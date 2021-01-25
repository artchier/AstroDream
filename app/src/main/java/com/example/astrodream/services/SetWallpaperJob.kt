package com.example.astrodream.services

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SetWallpaperJob : JobService() {

    companion object {
        const val TAG = "SetWallpaperJob"
    }

    @SuppressLint("SimpleDateFormat")
    override fun onStartJob(params: JobParameters?): Boolean {

        Log.d(TAG, "Job iniciado")

        GlobalScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val calendar = Calendar.getInstance().apply { time = Date() }
            var failCount = 0
            var imageUrl = ""

            while (imageUrl == "") {
                when (val response = service.getDaily(dateFormat.format(calendar.time))) {
                    // Handle successful response
                    is NetworkResponse.Success -> {
                        if ((response.body.url).contains("youtube")) {
                            Log.d(TAG, "Imagem do youtube")
                            calendar.add(Calendar.DATE, -1)
                        } else {
                            imageUrl = response.body.hdurl
                            Log.d(TAG, "Imagem encontrada: $imageUrl")
                        }
                    }
                    // Handle server error (unavailable date falls into ServerError)
                    is NetworkResponse.ServerError -> {
                        Log.d(TAG, "Server error")
                        if (failCount++ >= 5) {
                            break
                        }
                        calendar.add(Calendar.DATE, -1)
                    }
                    // Qualquer outro tipo de resposta consideramos erro
                    else -> {
                        Log.d(TAG, "Generic error")
                        break
                    }
                }
            }

            // Falhamos em buscar uma imagem, remarcamos o job
            if (imageUrl == "") {
                Log.d(TAG, "Remarcando job")
                jobFinished(params, true)
                return@launch
            }

            Glide.with(baseContext)
                .load(imageUrl)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        Log.d(TAG, "Imagem baixada, usando como wallpaper")
                        val extras = params!!.extras
                        val screenSize = Point(extras.getInt("width"), extras.getInt("height"))

                        setImageAsWallpaper(screenSize, baseContext, resource)
                        // Terminamos o Job de fato
                        jobFinished(params, false)
                        Log.d(TAG, "Job acabado!")
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }

        // Indica que o Job não está feito, já que é feito async
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        // A internet caiu enquanto baixávamos a imagem. True quer dizer que queremos remarcar o Job
        Log.d(TAG, "Job interrompido")
        return true
    }

}