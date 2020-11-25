package com.example.astrodream

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.user_email_password.view.*

private const val ARG_EMAIL = "email"
private const val ARG_PASSWORD = "password"

class SignInFragment : Fragment() {

    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(ARG_EMAIL)
            password = it.getString(ARG_PASSWORD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        email?.apply {
            view.tiEmail.setText(this)
        }

        password?.apply {
            view.tiPassword.setText(this)
        }

        // TODO fazer sign in de verdade
        view.findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            startActivity(Intent(activity, InitialActivity::class.java))
            activity?.finish()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String, password: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMAIL, email)
                    putString(ARG_PASSWORD, password)
                }
            }
    }
}