package com.example.astrodream.domain.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ExpandableListView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun ExpandableListView.isSomeGroupExpandad(): Boolean {
    if (!this.isGroupExpanded(0) || !this.isGroupExpanded(1) ||
            !this.isGroupExpanded(2) || !this.isGroupExpanded(3)) return true
    return false
}