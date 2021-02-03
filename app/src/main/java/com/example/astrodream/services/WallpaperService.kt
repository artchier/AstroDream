package com.example.astrodream.services

import android.app.WallpaperManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.saveImage

const val WALLPAPER_JOB_ID = 10001
const val TAG = "WallpaperService"

fun setImageAsWallpaper(screenSize: Point, context: Context, image: Drawable) {
    val bitmap = image.toBitmap()

    val cropY: Float
    val cropX: Float

    if (bitmap.height / bitmap.height <= screenSize.y / screenSize.x) {
        cropY = bitmap.height.toFloat()
        cropX = (screenSize.x.toFloat() / screenSize.y.toFloat()) * cropY
    }
    else {
        cropX = bitmap.width.toFloat()
        cropY = (screenSize.y.toFloat() / screenSize.x.toFloat()) * cropX
    }

    val manager = WallpaperManager.getInstance(context)
    manager.setBitmap(
        Bitmap.createBitmap(
            bitmap,
            (bitmap.width - cropX.toInt()) / 2,
            (bitmap.height - cropY.toInt()) / 2,
            cropX.toInt(),
            cropY.toInt()
        )
    )
}

fun scheduleWallpaperChange(context: Context) {
    val componentName = ComponentName(context, SetWallpaperJob::class.java)
    val builder = JobInfo.Builder(WALLPAPER_JOB_ID, componentName)

    val screenSize = Point()
    context.display!!.getRealSize(screenSize)

    val bundle = PersistableBundle().apply {
        putInt("height", screenSize.y)
        putInt("width", screenSize.x)
    }

    builder.setPersisted(true)
        .setPeriodic(86400000) // Milisegundos em um dia
        .setBackoffCriteria(900000, JobInfo.BACKOFF_POLICY_LINEAR) // Milisegndos em 15 min
        .setRequiresCharging(false)
        .setRequiresDeviceIdle(false)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setExtras(bundle)

    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    jobScheduler.schedule(builder.build())

    Log.d(TAG, "Job para mudar papel de parede marcado")
}

fun cancelWallpaperChange(context: Context) {
    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    jobScheduler.cancel(WALLPAPER_JOB_ID)

    Log.d(TAG, "Job para mudar papel de parede cancelado")
}

fun buildDownloadSetWallpaperMenu(context: Context, anchor: View, imageUrl: String) {
    PopupMenu(context, anchor, Gravity.TOP).apply {
        inflate(R.menu.menu_fullscreen_images)
        setOnMenuItemClickListener {
            Toast.makeText(context, "Baixando imagem em alta resolução...", Toast.LENGTH_SHORT).show()

            Glide.with(context)
                .load(imageUrl)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        when (it.itemId) {
                            R.id.downloadImageItem -> {
                                AstroDreamUtil.saveImage(resource.toBitmap(), context,
                                    context.getString(R.string.app_name))

                                Toast.makeText(context, "Imagem salva!", Toast.LENGTH_SHORT).show()
                            }
                            R.id.useAsWallpaperItem -> {
                                val screenSize = Point()
                                context.display!!.getRealSize(screenSize)

                                setImageAsWallpaper(screenSize, context, resource)
                                Toast.makeText(context, "Wallpaper atualizado!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            true
        }
    }.show()
}
