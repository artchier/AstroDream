package com.example.astrodream

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


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