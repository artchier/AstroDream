package com.example.astrodream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        callFragLogin()

        btnLoginFrag.setOnClickListener {
            markButtonSelected(btnLoginFrag, btnSignInFrag)
            callFragLogin()
        }

        btnSignInFrag.setOnClickListener {
            markButtonSelected(btnSignInFrag, btnLoginFrag)
            callFragSignIn()
        }
    }

    private fun callFragLogin() {
        val fragLogin = LoginFragment.newInstance()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragLogin)
            commit()
        }
    }

    private fun callFragSignIn() {
        val userEmail = findViewById<TextInputEditText>(R.id.tiEmail).text.toString()
        val userPassword = findViewById<TextInputEditText>(R.id.tiPassword).text.toString()

        val fragSignIn = SignInFragment.newInstance(userEmail, userPassword)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragSignIn)
            commit()
        }
    }

    private fun markButtonSelected(btnSelected: AppCompatButton, btnUnselected: AppCompatButton) {
        btnSelected.background = ContextCompat.getDrawable(this, R.drawable.pill_shape)
        btnSelected.setTextColor(ContextCompat.getColor(this, R.color.lucky_point))

        btnUnselected.background = ContextCompat.getDrawable(this, R.color.transparent)
        btnUnselected.setTextColor(ContextCompat.getColor(this, R.color.light_purple))
    }
}