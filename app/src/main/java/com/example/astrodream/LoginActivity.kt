package com.example.astrodream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        clLogin.setOnClickListener {
            markButtonSelected(clLogin, tvLogin, clSignIn, tvSignIn)
        }

        clSignIn.setOnClickListener {
            markButtonSelected(clSignIn, tvSignIn, clLogin, tvLogin)
        }
    }

    private fun markButtonSelected(containerS: ConstraintLayout, textS: TextView, containerU: ConstraintLayout, textU: TextView) {
        // Botão sendo marcado como selecionado
        containerS.background.setTint(ContextCompat.getColor(this, R.color.light_purple))
        textS.setTextColor(ContextCompat.getColor(this, R.color.lucky_point))

        // Botão sendo marcado como não selecionado
        containerU.background.setTint(ContextCompat.getColor(this, R.color.transparent))
        textU.setTextColor(ContextCompat.getColor(this, R.color.light_purple))
    }
}