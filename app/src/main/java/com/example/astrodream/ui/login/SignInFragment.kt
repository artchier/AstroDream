package com.example.astrodream.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.astrodream.R
import com.example.astrodream.ui.initial.InitialActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.user_email_password.*

class SignInFragment : FragmentWithEmailAndPassword(R.layout.fragment_sign_in) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // TODO fazer sign in de verdade
        view.findViewById<Button>(R.id.btnSignIn).setOnClickListener {
//            startActivity(Intent(activity, InitialActivity::class.java))
//            activity?.finish()
            val email = tiEmail.text.toString()
            val password = tiPassword.text.toString()
            createUserFirebase(email, password)
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

    private fun createUserFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result?.user!!
                    val key = firebaseUser.uid
                    val emailFire = firebaseUser.email.toString()

//                    showLongMessage("Registration successful")

                    startActivity(Intent(activity, InitialActivity::class.java).apply {
                        putExtra("key", key)
                        putExtra("email", emailFire)
                    })
                    activity?.finish()
                }
                else {
//                    showLongMessage(task.exception.toString())
                }
            }
    }
}