package com.example.astrodream.ui

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.astrodream.R
import com.example.astrodream.ui.asteroids.AsteroidActivity
import com.example.astrodream.ui.avatar.AvatarActivity
import com.example.astrodream.ui.dailyimage.DailyImageActivity
import com.example.astrodream.ui.favorites.FavoritesActivity
import com.example.astrodream.ui.globe.GlobeActivity
import com.example.astrodream.ui.initial.InitialActivity
import com.example.astrodream.ui.login.LoginActivity
import com.example.astrodream.ui.mars.MarsActivity
import com.example.astrodream.ui.tech.TechActivity
import com.example.astrodream.ui.userconfig.UserConfigActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.app_tool_bar.*
import kotlinx.android.synthetic.main.header_layout.*

abstract class ActivityWithTopBar(
    private val toolbarTitleId: Int,
    private val drawerLayoutId: Int
) : AppCompatActivity() {
    private var toolBar: MaterialToolbar? = null
    private lateinit var drawerLayout: DrawerLayout

    val realtimeViewModel: RealtimeViewModel by viewModels()

    private fun userListener(name: String, email: String) {
        if (email == "") {
            return
        }

        realtimeViewModel.retrieveUserData(email, name)

        realtimeViewModel.activeUser.observe(this) {
            ivAstronauta.setImageDrawable(
                ContextCompat.getDrawable(this, it.avatar)
            )
            tvUserName.text = getString(R.string.greetins).format(it.name)
        }
    }

    private fun <T> goToActivityIfNotAlreadyThere(destination: Class<T>) {
        if (this::class.java == destination) {
            drawerLayout.closeDrawer(GravityCompat.END)
            return
        }
        startActivity(Intent(this, destination))
        if (this !is InitialActivity) {
            finish()
        }
    }

    fun setUpMenuBehavior() {
        toolBar = if (toolBar == null) appToolBar else toolBar
        drawerLayout = findViewById(drawerLayoutId)

        toolBar?.apply {
            title = ""
            tvToolBarTitle.text = resources.getString(toolbarTitleId)
        }
        setSupportActionBar(toolBar)

        val lateralMenuHost = findViewById<NavigationView>(R.id.nvLateralMenu)
        val lateralMenu = findViewById<ScrollView>(R.id.clLateralMenu)

        val btnAvatar = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnAvatar)

        val llBackground = lateralMenu.findViewById<LinearLayout>(R.id.llBackground)
        val llAsteroides = lateralMenu.findViewById<LinearLayout>(R.id.llAsteroides)
        val llGlobo = lateralMenu.findViewById<LinearLayout>(R.id.llGlobo)
        val llTecnologias = lateralMenu.findViewById<LinearLayout>(R.id.llTecnologias)
        val llMarte = lateralMenu.findViewById<LinearLayout>(R.id.llMarte)

        val btnFavoritos = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnFavoritos)
        val btnConfig = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnConfig)
        val btnSobre = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnSobre)
        val btnLogout = lateralMenuHost.findViewById<AppCompatButton>(R.id.btnLogout)

        btnAvatar.setOnClickListener {
            goToActivityIfNotAlreadyThere(AvatarActivity::class.java)
        }

        llBackground.setOnClickListener {
            goToActivityIfNotAlreadyThere(DailyImageActivity::class.java)
        }

        llAsteroides.setOnClickListener {
            goToActivityIfNotAlreadyThere(AsteroidActivity::class.java)
        }

        llGlobo.setOnClickListener {
            goToActivityIfNotAlreadyThere(GlobeActivity::class.java)
        }

        llTecnologias.setOnClickListener {
            goToActivityIfNotAlreadyThere(TechActivity::class.java)
        }

        llMarte.setOnClickListener {
            goToActivityIfNotAlreadyThere(MarsActivity::class.java)
        }

        btnFavoritos.setOnClickListener {
            goToActivityIfNotAlreadyThere(FavoritesActivity::class.java)
        }

        btnConfig.setOnClickListener {
            goToActivityIfNotAlreadyThere(UserConfigActivity::class.java)
        }

        btnSobre.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setBackgroundInsetStart(70)
                .setBackgroundInsetEnd(70)
                .setBackgroundInsetTop(10)
                .setBackgroundInsetBottom(100)
                .setBackground(
                    ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
                )
                .setView(R.layout.astrodialog)
                .show()
        }

        btnLogout.setOnClickListener {
            logout()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val providerData: List<UserInfo?> = currentUser.providerData
            val email = providerData[1]!!.email
            userListener(
                Firebase.auth.currentUser?.displayName ?: "",
                email ?: ""
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_lateral) {
            drawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
            return
        }
        super.onBackPressed()
    }

    private fun logout() {
        // Logout email/senha
        Firebase.auth.signOut()
        // Logout Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("67163504194-hjucfh631cgv65fuegvfqbp51n016lg0.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
        // Logout Facebook
        // TODO: parece que não está funcionando direito....
        LoginManager.getInstance().logOut()

        //essa linha garante que a tela de login seja exibida se caso o usuário faça logout e saia do app
        // por meio do botão de voltar e não o encerre
        SplashScreenActivity.alreadyLogged = true

        // Volta para a activity de login
        goToActivityIfNotAlreadyThere(LoginActivity::class.java)
        finish()
    }
}