package com.example.astrodream.ui.userconfig

import android.app.AlertDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.viewModelScope
import com.example.astrodream.R
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.RealtimeViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_config.*
import kotlinx.android.synthetic.main.dialog_confirm_current_pswd.*
import kotlinx.android.synthetic.main.dialog_confirm_current_pswd.view.*
import kotlinx.android.synthetic.main.fragment_user_config.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.header_layout.tvUserName
import kotlinx.coroutines.launch

class UserConfigActivity : ActivityWithTopBar(R.string.user_config, R.id.dlUserConfig), ContractUserConfigActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_config)
        setUpMenuBehavior()

    }

    override val rvtm: RealtimeViewModel
        get() = realtimeViewModel

}