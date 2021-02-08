package com.example.astrodream.domain.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.astrodream.R
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.entitiesDatabase.DailyRoom
import com.example.astrodream.entitiesDatabase.MarsRoom
import com.example.astrodream.ui.initial.InitialActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.security.MessageDigest

class AstroDreamUtil {
    companion object
}

fun AstroDreamUtil.Companion.showDialogMessage(context: Context, id_layout: Int) {
    val view: View = LayoutInflater.from(context).inflate(id_layout, null)

    run {
       val dialog = MaterialAlertDialogBuilder(context)
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

fun AstroDreamUtil.Companion.showDialogError(context: Context, id_layout: Int) {
    val view: View = LayoutInflater.from(context).inflate(id_layout, null)

    run {
        val dialog = MaterialAlertDialogBuilder(context)
            .setBackgroundInsetStart(70)
            .setBackgroundInsetEnd(70)
            .setBackgroundInsetTop(10)
            .setBackgroundInsetBottom(100)
            .setCancelable(false)
            .setBackground(
                ContextCompat.getColor(context, android.R.color.transparent).toDrawable()
            )
            .setView(view)
            .create()

        view.findViewById<Button>(R.id.button_error_message).setOnClickListener {
            context.startActivity(Intent(context, InitialActivity::class.java))
            dialog.dismiss()
            (context as Activity).finish()
        }

        dialog.show()
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

fun AstroDreamUtil.Companion.formatDate(day: String, month: String, year: String): String{
    if (day.length == 1 && month.length == 1) return "0$day/0$month/$year"
    if (day.length == 1) return "0$day/$month/$year"
    if (month.length == 1) return "$day/0$month/$year"
    return "$day/$month/$year"
}

fun AstroDreamUtil.Companion.saveImage(bitmap: Bitmap, context: Context, fileName: String): String {
    val parentDirectory = File(context.filesDir, context.getString(R.string.app_name))

    if (!parentDirectory.exists()) {
        parentDirectory.mkdirs()
    }

    val file = File(parentDirectory, fileName)
    if (file.exists()) {
        return file.absolutePath.toString()
    }
    try {
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        Log.w("SaveImage", "Erro ao salvar imagem $fileName. Erro:\n${e.message}")
        return ""
    }

    return file.absolutePath.toString()
}

fun AstroDreamUtil.Companion.saveImageGallery(
    bitmap: Bitmap,
    context: Context,
    folderName: String,
    fileName: String = ""
): String {
    val fileUri: String

    if (Build.VERSION.SDK_INT >= 29) {
        val values = contentValues()
        if (fileName != "") {
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.png");
        }
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

fun ExpandableListView.collapseAllGroups() {
    this.collapseGroup(0)
    this.collapseGroup(1)
    this.collapseGroup(2)
    this.collapseGroup(3)
}

fun AstroDreamUtil.Companion.returnTextOf(vararg string: String): String{
    val sb = StringBuilder()
    string.forEach { sb.append("\n$it") }
    return sb.toString()
}

fun AstroDreamUtil.Companion.isPotentiallyHazardousAsteroid(yesOrNot: String): Boolean {
    when (yesOrNot) {
        "Y" -> return true
        else -> return false
    }
}

@SuppressLint("ServiceCast")
fun AstroDreamUtil.Companion.isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
}

fun AstroDreamUtil.Companion.showErrorInternetConnection(context: Context){
        AstroDreamUtil.showDialogError(context, R.layout.internet_connection_error)
}

fun AstroDreamUtil.Companion.showUnknownError(context: Context){
    AstroDreamUtil.showDialogError(context, R.layout.unknown_error_dialog)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

fun AstroDreamUtil.Companion.useGlide(context: Context,
                                      image: Any,
                                      onResourceReady: (resource: Drawable) -> Unit) {
    Glide.with(context)
        .load(image)
        .into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable?>?
            ) {
                onResourceReady(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}