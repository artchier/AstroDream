package com.example.astrodream.ui.userconfig

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import com.example.astrodream.domain.util.hideKeyboard
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_change_password.view.*

class UserChangePasswordFragment : Fragment() {

    private lateinit var user: FirebaseUser
    private lateinit var userUID: String
    private lateinit var userEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_user_change_password, container, false)
        user = Firebase.auth.currentUser!!
        userEmail = user.email.toString()

        view.btnConfirmCurrentPswd.setOnClickListener {
            val currentPassword = view.tiCurrentPswd.text.toString()
            val newPassword = view.tiNewPswd.text.toString()
            val newPasswordConfirmation = view.tiConfirmNewPswd.text.toString()
            if (currentPassword == "" || newPassword == "" || newPasswordConfirmation == "") {
                Toast.makeText(requireContext(), "Por favor preencha todos os campos!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (newPassword != newPasswordConfirmation) {
                Toast.makeText(requireContext(), "As senhas digitadas não coincidem. Por favor tente novamente.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            confirmCurrentPassword(Firebase.auth.currentUser!!, userEmail, currentPassword, newPassword)
        }

        view.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun confirmCurrentPassword(user: FirebaseUser, email: String, password: String, newPassword: String) {
        val credential = EmailAuthProvider
            .getCredential(email, password)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                Log.d("===============", "User re-authenticated.")
                changePassword(user, newPassword)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Senha incorreta!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun changePassword(user: FirebaseUser, newPassword: String) {
        user.updatePassword(newPassword)
            .addOnSuccessListener {
                Toast.makeText(activity, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                hideKeyboard()
            }
    }
}