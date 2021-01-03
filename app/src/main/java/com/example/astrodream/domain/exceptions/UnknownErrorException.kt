package com.example.astrodream.domain.exceptions

import android.content.Context
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.showDialogMessage

class UnknownErrorException: Exception() {
    fun showImageUnknownError(context: Context){ AstroDreamUtil.showDialogMessage(context, R.layout.unknown_error_dialog) }
}