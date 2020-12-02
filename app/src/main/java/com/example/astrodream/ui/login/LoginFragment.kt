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


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // TODO fazer login de verdade
        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            startActivity(Intent(activity, InitialActivity::class.java))
            activity?.finish()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}