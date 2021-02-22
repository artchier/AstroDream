package com.example.astrodream.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.astrodream.R
import com.example.astrodream.domain.TranslatorEngToPort
import com.example.astrodream.ui.initial.InitialActivity
import com.example.astrodream.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    companion object {
        var alreadyLogged = true
        var showingLoginActivity = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val currentUser = FirebaseAuth.getInstance().currentUser
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            when {
                //primeira vez em que é checado se o usuário está logado
                currentUser != null && alreadyLogged -> {
                    val providerData: List<UserInfo?> = currentUser.providerData
                    val email = providerData[1]!!.email
                    callInitialActivity(email)
                }
                //qualquer tentativa de login além da primeira é barrada por essa condição
                !alreadyLogged -> {
                    finish()
                }
                //se não houver usuário logado, por padrão a SplashScreen segue para a tela de Login
                !showingLoginActivity -> {
                    callLoginActivity()
                    showingLoginActivity = true
                }
            }
        }
        // Desabilita o modo noturno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        TranslatorEngToPort
    }

    private fun callInitialActivity(email: String?) {
        startActivity(Intent(this, InitialActivity::class.java).apply {
            putExtra("email", email)
        })
        //essa linha garante que não haverá mais nenhuma tentativa de login validada além da primeira
        alreadyLogged = false
        finish()
    }

    private fun callLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}