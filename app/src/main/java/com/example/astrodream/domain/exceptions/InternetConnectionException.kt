package com.example.astrodream.domain.exceptions

import android.content.Context
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.showDialogMessage

class InternetConnectionException: Exception() {
    fun showImageWithoutInternetConnection(context: Context) { AstroDreamUtil.showDialogMessage(context, R.layout.without_internet_connection_dialog) }
}