package com.example.astrodream.services

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

fun setImageAsWallpaper(context: Context, image: Drawable) {
    val screenSize = Point()
    context.display!!.getRealSize(screenSize)

    val bitmap = image.toBitmap()

    var cropY = bitmap.height.toFloat()
    var cropX = (screenSize.x.toFloat() / screenSize.y.toFloat()) * cropY

    if (cropX > bitmap.width) {
        cropY = cropY * bitmap.width.toFloat() / cropX
        cropX = bitmap.width.toFloat()
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