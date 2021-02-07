package com.example.astrodream.services

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.useGlide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


fun shareText(title: String, description: String, context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, buildDescription(title, description))
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartilhar texto"))
}

fun shareImageFromUrl(url: String, title: String, description: String, context: Context) {
    Toast.makeText(context, "Baixando imagem em alta resolução...", Toast.LENGTH_SHORT).show()

    AstroDreamUtil.useGlide(context, url) { resource ->
        shareImageFromBitmap(resource.toBitmap(), title, description, context)
    }
}

fun shareImageFromBitmap(image: Bitmap, title: String, description: String, context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(image, context))
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, buildDescription(title, description))
        addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartilhar imagem"))
}

private fun buildDescription(title: String, description: String): String {
    var body = title
    if (description != "") {
        body += "\n\n${description.replace(Regex(" {2,}"), " ")}"
    }

    return "$body\n\nCompartilhado pelo app AstroDream"
}

private fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
    var bmpUri: Uri? = null
    try {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + UUID.randomUUID() + ".png")
        val out = FileOutputStream(file)

        bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.close()
        bmpUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}
