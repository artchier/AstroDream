package com.example.astrodream.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.astrodream.R
import com.example.astrodream.ui.initial.InitialActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_email_password.*

class LoginFragment : FragmentWithEmailAndPassword(R.layout.fragment_login) {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        auth = Firebase.auth

        startLoginButtonFacebook(view)

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            login()
        }

        view.findViewById<Button>(R.id.btnForgotPassword).setOnClickListener {
            callFragResetPswd(view)
        }

        view.findViewById<ImageButton>(R.id.btnLoginGoogle).setOnClickListener {
            loginWithGoogle()
        }

        view.findViewById<LoginButton>(R.id.btnLoginFacebook).registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    firebaseAuthWithFacebook(result.accessToken)
                    Toast.makeText(requireContext(), "Logando...", Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException?) {
                    Toast.makeText(requireContext(), "Erro no login com Facebook, tente novamente!", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", error.toString())
                }

            })

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("67163504194-hjucfh631cgv65fuegvfqbp51n016lg0.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String, password: String) =
            LoginFragment().apply {
                arguments = createBundle(email, password)
            }

        val callbackManager: CallbackManager = CallbackManager.Factory.create()
        const val RC_SIGN_IN_GOOGLE = 120
    }

    private fun callFragResetPswd(view: View) {
        val insertedEmail = view.findViewById<TextInputEditText>(R.id.tiEmail).text.toString()

        val fragSignIn = ResetPswdFragment.newInstance(insertedEmail, "")
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragSignIn)
            commit()
        }
    }

    private fun login() {
        val email = tiEmail.text.toString()
        val password = tiPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callInitialActivity()
                } else {
                    Log.i("Login", task.exception.toString())
                    if (task.exception is FirebaseAuthInvalidUserException || task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Erro inesperado, tente novamente mais tarde!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun loginWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(requireContext(), "Erro inesperado, tente novamente mais tarde!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val profile: Map<*, *>? = task.result!!.additionalUserInfo!!.profile
                    val email = profile!!["email"] as String
                    callInitialActivity(email)
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(requireContext(), "Já existe uma conta associada a este e-mail.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "Erro inesperado, tente novamente mais tarde!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun firebaseAuthWithFacebook(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val profile: Map<*, *>? = task.result!!.additionalUserInfo!!.profile
                    val email = profile!!["email"] as String
                    callInitialActivity(email)
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(requireContext(), "Já existe uma conta associada a este email.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "Erro inesperado, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun startLoginButtonFacebook(view: View) {
        view.findViewById<LoginButton>(R.id.btnLoginFacebook)
            .setReadPermissions("email", "public_profile")
    }

    private fun callInitialActivity() {
        startActivity(Intent(requireActivity(), InitialActivity::class.java))
        activity?.finish()
        Toast.makeText(requireContext(), "Logando...", Toast.LENGTH_SHORT).show()
    }

    private fun callInitialActivity(email: String) {
        val intent = Intent(requireActivity(), InitialActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        activity?.finish()
        Toast.makeText(requireContext(), "Logando...", Toast.LENGTH_SHORT).show()
    }

}