package com.example.astrodream.domain.exceptions

import android.content.Context
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.showDialogError
import com.example.astrodream.domain.util.showDialogMessage

class ServerErrorException : Exception() {
    fun showImageServerError(context: Context){ AstroDreamUtil.showDialogError(context, R.layout.server_error_dialog) }
}