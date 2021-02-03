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
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.coroutines.launch

class UserConfigActivity : ActivityWithTopBar(R.string.user_config, R.id.dlUserConfig) {

    private lateinit var user: FirebaseUser
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_config)
        setUpMenuBehavior()

        user = Firebase.auth.currentUser!!
        userName = user.displayName.toString()

        realtimeViewModel.activeUser.observe(this) {
            ivAvatar.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    it.avatar
                )
            )
            tiName.setText(it.name)
        }

        tiPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val confirmPasswordView =
                    View.inflate(this, R.layout.dialog_confirm_current_pswd, null)
                val confirmPasswordDialog = showDialog(confirmPasswordView)
                confirmPasswordView.tiConfirmPassword.requestFocus()
                confirmPasswordView.btnCurrentPswd.setOnClickListener {
                    confirmCurrentPassword(
                        Firebase.auth.currentUser!!,
                        Firebase.auth.currentUser!!.email ?: "",
                        confirmPasswordView.tiConfirmPassword.text.toString()
                    )
                }
                confirmPasswordDialog.dismiss()

            }
        }

        btnAlterarDados.setOnClickListener {
            if (userName != tiName.text.toString()) {
                realtimeViewModel.updateUserName(user.uid, tiName.text.toString())
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(tiName.text.toString())
                    .build()
                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("===============", "User profile updated.")
                            userName = user.displayName.toString()
                        }
                    }
            }
            Toast.makeText(this, tiPassword.text.toString(), Toast.LENGTH_SHORT).show()
            if (tiPassword.text.toString() != "") {
                changePassword(user, tiPassword.text.toString())
            }
        }

        tiName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tilName.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                tilName.endIconDrawable?.setColorFilter(resources.getColor(R.color.lucky_point), PorterDuff.Mode.SRC_ATOP)
                tilName.background.setColorFilter(resources.getColor(R.color.light_purple), PorterDuff.Mode.SRC_ATOP)
                tiName.setTextColor(getColor(R.color.black))
            } else {
                tilName.endIconMode = TextInputLayout.END_ICON_CUSTOM
                tilName.endIconDrawable = getDrawable(R.drawable.ic_edit)
                tilName.endIconDrawable?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
                tilName.background.setColorFilter(resources.getColor(R.color.transparent), PorterDuff.Mode.CLEAR)
                tiName.setTextColor(getColor(R.color.white))
            }
        }

//        tilUserName.setEndIconOnClickListener {
//            tiNome.isEnabled = true
//            tiNome.requestFocus()
//            Toast.makeText(this, "${tiNome.back}", Toast.LENGTH_SHORT).show()
//        }
    }

    fun confirmCurrentPassword(user: FirebaseUser, email: String, password: String) {
        val credential = EmailAuthProvider
            .getCredential(email, password)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                    Log.d("===============", "REALTIMEVIEWMODEL User re-authenticated.")
                    tiPassword.isEnabled = true
            }
            .addOnFailureListener {
                Toast.makeText(this, "Senha incorreta! $email", Toast.LENGTH_SHORT).show()
            }
    }

    fun changePassword(user: FirebaseUser, newPassword: String) {
        user.updatePassword(newPassword)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("===============", "REALTIMEVIEWMODEL User password updated.")
                    Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun showDialog(dialogView: View) = MaterialAlertDialogBuilder(this)
            .setBackground(
                ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
            )
            .setView(dialogView)
            .setBackgroundInsetStart(70)
            .setBackgroundInsetEnd(70)
            .setBackgroundInsetTop(10)
            .setBackgroundInsetBottom(100)
            .show()

}