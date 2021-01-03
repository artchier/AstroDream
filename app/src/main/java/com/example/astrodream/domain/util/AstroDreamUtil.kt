package com.example.astrodream.domain.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AstroDreamUtil {
    companion object
}

fun AstroDreamUtil.Companion.showDialogMessage(context: Context, id_layout: Int) {
    val li: LayoutInflater = LayoutInflater.from(context)
    val view: View = li.inflate(id_layout, null)

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