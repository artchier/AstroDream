package com.example.astrodream.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.astrodream.R
import com.example.astrodream.ui.initial.InitialActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var insertedEmail = ""
    private var insertedPassword= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(this, InitialActivity::class.java))
            finish()
        }

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
        val fragLogin = LoginFragment.newInstance(insertedEmail, insertedPassword)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragLogin)
            commit()
        }
    }

    private fun callFragSignIn() {
        insertedEmail = findViewById<TextInputEditText>(R.id.tiEmail).text.toString()
        insertedPassword = findViewById<TextInputEditText>(R.id.tiPassword).text.toString()

        val fragSignIn = SignInFragment.newInstance(insertedEmail, insertedPassword)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode != LoginFragment.RC_SIGN_IN_GOOGLE){
            LoginFragment.callbackManager.onActivityResult(
                requestCode,
                resultCode,
                data
            )
        }
        //Toast.makeText(this, "Passou aqui", Toast.LENGTH_SHORT).show()
    }
}