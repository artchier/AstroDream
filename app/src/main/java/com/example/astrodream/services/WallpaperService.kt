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

fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
    if (android.os.Build.VERSION.SDK_INT >= 29) {
        val values = contentValues()
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        // RELATIVE_PATH and IS_PENDING are introduced in API 29.

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
        }
    } else {
        val directory = File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName)
        // getExternalStorageDirectory is deprecated in API 29

        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(directory, fileName)
        saveImageToStream(bitmap, FileOutputStream(file))
        val values = contentValues()
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        // .DATA is deprecated in API 29
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }
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

private fun contentValues() : ContentValues {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    return values
}

private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
