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
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.user_email_password.*

class LoginFragment : FragmentWithEmailAndPassword(R.layout.fragment_login) {

    private lateinit var googleSignInClient: GoogleSignInClient
    /*private val RC_SIGN_IN_GOOGLE = 120*/
    private lateinit var auth: FirebaseAuth

    //private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        auth = Firebase.auth

        startLoginButtonFacebook(view)
        //firebaseCallback()

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            login()
        }

        view.findViewById<ImageButton>(R.id.btnLoginGoogle).setOnClickListener {
            loginWithGoogle()
        }

        /*view.findViewById<LoginButton>(R.id.btnLoginFacebook).setOnClickListener {

        }*/

        view.findViewById<LoginButton>(R.id.btnLoginFacebook).registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    firebaseAuthWithFacebook(result.accessToken)
                    Toast.makeText(requireContext(), "Caí aqui!", Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    Toast.makeText(
                        requireContext(),
                        "Cancelado o login com Facebook!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(
                        requireContext(),
                        "Erro no login com Facebook!",
                        Toast.LENGTH_SHORT
                    ).show()
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
        val RC_SIGN_IN_GOOGLE = 120
    }

    private fun login() {
        val email = tiEmail.text.toString()
        val password = tiPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result?.user!!
                    val id = firebaseUser.uid
                    val emailFire = firebaseUser.email.toString()

                    startActivity(Intent(activity, InitialActivity::class.java).apply {
                        putExtra("id", id)
                        putExtra("email", emailFire)
                    })
                    activity?.finish()
                } else {
                    // TODO: mostrar dialog?
//                    showLongMessage(task.exception.toString())
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
                Log.d("=====LOGIN=====", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("=====LOGIN=====", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    startActivity(Intent(activity, InitialActivity::class.java).apply {
//                        putExtra("id", id)
//                        putExtra("email", emailFire)
                    })
                    activity?.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    // TODO: mostrar dialog?
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
//                    updateUI(null)
                }

            }
    }

    private fun firebaseAuthWithFacebook(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    startActivity(Intent(activity, InitialActivity::class.java).apply {

                    })
                    activity?.finish()
                } else {
                    // TODO: mostrar dialog?
                }
            }
    }

    private fun startLoginButtonFacebook(view: View) {
        view.findViewById<LoginButton>(R.id.btnLoginFacebook)
            .setReadPermissions("email", "public_profile", "user_friends")
    }

    /*private fun firebaseCallback() {
        callbackManager = CallbackManager.Factory.create()
    }*/
}