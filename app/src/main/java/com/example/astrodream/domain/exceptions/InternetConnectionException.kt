package com.example.astrodream.domain.exceptions

import android.content.Context
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.showDialogError
import com.example.astrodream.domain.util.showDialogMessage

class InternetConnectionException: Exception() {
    fun showImageWithoutInternetConnection(context: Context) { AstroDreamUtil.showDialogError(context, R.layout.internet_connection_error) }
}