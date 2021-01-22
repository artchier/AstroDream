package com.example.astrodream.domain.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.request.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.widget.ExpandableListView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.entitiesDatabase.DailyRoom
import com.example.astrodream.entitiesDatabase.MarsRoom
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class AstroDreamUtil {
    companion object
}

fun AstroDreamUtil.Companion.showDialogMessage(context: Context, id_layout: Int) {
    val view: View = LayoutInflater.from(context).inflate(id_layout, null)

    run {
        MaterialAlertDialogBuilder(context)
            .setBackgroundInsetStart(70)
            .setBackgroundInsetEnd(70)
            .setBackgroundInsetTop(10)
            .setBackgroundInsetBottom(100)
            .setBackground(
                ContextCompat.getColor(context, android.R.color.transparent).toDrawable()
            )
            .setView(view)
            .show()
    }
}

fun AstroDreamUtil.Companion.showDialogMessage(context: Context, view: View) {
    run {
        MaterialAlertDialogBuilder(context)
            .setBackgroundInsetStart(70)
            .setBackgroundInsetEnd(70)
            .setBackgroundInsetTop(10)
            .setBackgroundInsetBottom(100)
            .setBackground(
                ContextCompat.getColor(context, android.R.color.transparent).toDrawable()
            )
            .setView(view)
            .show()
    }
}

fun AstroDreamUtil.Companion.formatDate(day: Int, month: Int, year: Int): String{
    if (day.toString().length == 1 && month.toString().length == 1) return "0$day/0$month/$year"
    if (day.toString().length == 1) return "0$day/$month/$year"
    if (month.toString().length == 1) return "$day/0$month/$year"
    return "$day/$month/$year"
}

fun AstroDreamUtil.Companion.saveImage(bitmap: Bitmap, context: Context, folderName: String, fileName: String): String {
    var fileUri: String = ""

    if (android.os.Build.VERSION.SDK_INT >= 29) {
        val values = contentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        // RELATIVE_PATH and IS_PENDING are introduced in API 29.

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        fileUri = uri.toString()
        if (uri != null) {
            saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            Log.i("====UTIL-SAVEIMAGE>29==", fileUri.toString())
        }
    } else {
        val directory = File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName)
        // getExternalStorageDirectory is deprecated in API 29

        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "$fileName.png")
        saveImageToStream(bitmap, FileOutputStream(file))
        val values = contentValues()
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        // .DATA is deprecated in API 29
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        Log.i("====UTIL-SAVEIMAGE==", file.absolutePath.toString())
        fileUri = file.absolutePath.toString()
    }
    return fileUri
}

fun contentValues() : ContentValues {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    return values
}

fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun AstroDreamUtil.Companion.transformPlainToDailyDBClass(plainDetail: PlainClass, fileUri: String): DailyRoom {
    return DailyRoom(plainDetail.title, plainDetail.date, fileUri, plainDetail.explanation)
}

fun AstroDreamUtil.Companion.transformDailyDBClassToPlain(dailyRoom: DailyRoom): PlainClass {
    return PlainClass(title = dailyRoom.title, date = dailyRoom.date, url = dailyRoom.url, explanation = dailyRoom.explanation)
}

fun AstroDreamUtil.Companion.transformPlainToMarsDBClass(plainDetail: PlainClass): MarsRoom {
    return MarsRoom(plainDetail.earth_date, plainDetail.sol, plainDetail.maxTemp, plainDetail.minTemp)
}

fun AstroDreamUtil.Companion.transformMarsDBClassToPlain(marsRoom: MarsRoom): PlainClass {
    return PlainClass(earth_date = marsRoom.earth_date, sol = marsRoom.sol, maxTemp = marsRoom.maxTemp, minTemp = marsRoom.minTemp)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun ExpandableListView.isSomeGroupExpandad(): Boolean {
    if (this.isGroupExpanded(0) || this.isGroupExpanded(1) ||
            this.isGroupExpanded(2) || this.isGroupExpanded(3)) return true
    return false
}