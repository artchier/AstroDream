package com.example.astrodream.services

import android.app.WallpaperManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

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
