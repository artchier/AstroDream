package com.example.astrodream.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.astrodream.R
import com.example.astrodream.ui.initial.InitialActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_email_password.*

class LoginFragment : FragmentWithEmailAndPassword(R.layout.fragment_login) {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 120
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        auth = Firebase.auth

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            login()
        }

        view.findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            signIn()
        }

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
                arguments = creteBundle(email, password)
            }
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
                }
                else {
                    // TODO: mostrar dialog?
//                    showLongMessage(task.exception.toString())
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
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
}