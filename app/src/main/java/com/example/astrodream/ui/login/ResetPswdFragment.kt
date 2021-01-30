package com.example.astrodream.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.astrodream.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_email_password.*

class ResetPswdFragment : FragmentWithEmailAndPassword(R.layout.fragment_reset_pswd) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = super.onCreateView(inflater, container, savedInstanceState)

        view.findViewById<Button>(R.id.btnResetPswd).setOnClickListener {
            val email = tiEmail.text.toString()
            if (email != "") {
                resetPassword(email, view)
            } else {
                Toast.makeText(requireActivity(), "Por favor preencha seu email", Toast.LENGTH_LONG).show()
            }
        }

        view.findViewById<Button>(R.id.btnBackLogin).setOnClickListener {
            callFragLogin(view)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String, password: String) =
            ResetPswdFragment().apply {
                arguments = createBundle(email, password)
            }
    }


    private fun callFragLogin(view: View) {
        val insertedEmail = view.findViewById<TextInputEditText>(R.id.tiEmail).text.toString()
        val fragLogin = LoginFragment.newInstance(insertedEmail, "")
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragLogin)
            commit()
        }
    }

    private fun resetPassword(email: String, view: View) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Enviamos um link de redefinição de senha para o seu e-mai :)", Toast.LENGTH_LONG).show()
                    callFragLogin(view)
                } else {
                    Toast.makeText(requireContext(), "E-mail inválido. Por favor tente novamente.", Toast.LENGTH_LONG).show()
                }
            }
    }
}