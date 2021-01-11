package com.example.astrodream.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.astrodream.R
import com.example.astrodream.ui.initial.InitialActivity
import kotlinx.android.synthetic.main.user_email_password.view.*

class SignInFragment : FragmentWithEmailAndPassword(R.layout.fragment_sign_in) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

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
                arguments = creteBundle(email, password)
            }
    }
}