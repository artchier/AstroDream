package com.example.astrodream.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.astrodream.R
import com.example.astrodream.ui.initial.InitialActivity
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.user_email_password.*

class SignInFragment : FragmentWithEmailAndPassword(R.layout.fragment_sign_in) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        view.findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            val name = tiName.text.toString()
            val email = tiEmail.text.toString()
            val password = tiPassword.text.toString()
            val confirmPassword = tiConfirmPassword.text.toString()
            createUserFirebase(name, email, password, confirmPassword)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String, password: String) =
            SignInFragment().apply {
                arguments = createBundle(email, password)
            }
    }

    private fun createUserFirebase(name:String, email: String, password: String, confirmPassword: String) {
        if (name == "" || email == "" || password == "" || confirmPassword == "") {
            Toast.makeText(requireContext(), "Por favor preencha todos os campos!", Toast.LENGTH_LONG).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "As senhas digitadas não coincidem. Por favor tente novamente.", Toast.LENGTH_LONG).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result?.user!!
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val emailFire = firebaseUser.email.toString()

                    Toast.makeText(requireContext(), "Cadastro efetuado com sucesso! Seja muito bem-vindo(a)!", Toast.LENGTH_LONG).show()

                    startActivity(Intent(activity, InitialActivity::class.java).apply {
                        putExtra("uid", uid)
                        putExtra("name", name)
                        putExtra("email", emailFire)
                    })
                    activity?.finish()
                }
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthUserCollisionException -> {
                        Toast.makeText(requireContext(), "Já existe uma conta associada a este e-mail.", Toast.LENGTH_LONG).show()
                    }
                    is FirebaseAuthWeakPasswordException -> {
                        Toast.makeText(requireContext(), "Senha inválida (a senha precisa conter pelo menos 6 caracteres).", Toast.LENGTH_LONG).show()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(requireContext(), "E-mail inválido!", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Erro inesperado, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }
}