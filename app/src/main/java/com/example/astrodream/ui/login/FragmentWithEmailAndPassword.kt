package com.example.astrodream.ui.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.astrodream.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.user_email_password.view.*

private const val ARG_EMAIL = "email"
private const val ARG_PASSWORD = "password"

fun createBundle(email: String, password: String) =
    Bundle().apply {
        putString(ARG_EMAIL, email)
        putString(ARG_PASSWORD, password)
    }

abstract class FragmentWithEmailAndPassword(private val fragmentId: Int) : Fragment() {

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
    ): View {
        val view = inflater.inflate(fragmentId, container, false)

        email?.apply {
            view.tiEmail.setText(this)
        }

        password?.apply {
            if (view.tiPassword != null) { view.tiPassword.setText(this) }
        }

        return view
    }
}

