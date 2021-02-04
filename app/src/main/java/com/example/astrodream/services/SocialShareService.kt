package com.example.astrodream.services

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


fun shareImageFromUrl(url: String, title: String, description: String, context: Context) {
    Toast.makeText(context, "Baixando imagem em alta resolução...", Toast.LENGTH_SHORT).show()

    Glide.with(context)
        .load(url)
        .into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable?>?
            ) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource.toBitmap(), context))
                    putExtra(Intent.EXTRA_SUBJECT, title)
                    putExtra(Intent.EXTRA_TEXT, "$description\nCompartilhado pelo app AstroDream")
                    addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Compartilhar imagem"))
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
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
