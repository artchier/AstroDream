package com.example.astrodream.ui.avatar

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_avatar.*

class AvatarActivity : ActivityWithTopBar(R.string.avatar, R.id.dlAvatar) {
    private val avatarViewModel: AvatarViewModel by viewModels()
    private lateinit var adapter: AvatarAdapter
    private lateinit var buyAvatarView: View
    private lateinit var buyAvatarDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        //verifica se é a primeira vez que a tela de Avatar foi aberta
        if (getSharedPreferences("first_time", MODE_PRIVATE).getBoolean("avatar", true)) {
            avatarViewModel.initAllAvatarsAtRoom()
            getSharedPreferences("first_time", MODE_PRIVATE).edit().putBoolean("avatar", false)
                .apply()
        }

        //seta o total de NasaCoins que o usuário possui
        realtimeViewModel.activeUser.observe(this) {
            tvTotal.text = it.nasaCoins.toString()
        }

        //infla a dialog de compra de avatares
        buyAvatarView = View.inflate(this, R.layout.buy_avatar_dialog, null)

        buyAvatarDialog = MaterialAlertDialogBuilder(this)
            .setBackground(ContextCompat.getColor(this, android.R.color.transparent).toDrawable())
            .setView(buyAvatarView)
            .setBackgroundInsetStart(70)
            .setBackgroundInsetEnd(70)
            .setBackgroundInsetTop(10)
            .setBackgroundInsetBottom(100)
            .create()

        // Pega a lista de avatares do Room, que contém os IDs dos drawables e o preço de cada avatar
        avatarViewModel.getAllAvatarsFromRoom()
        // Quando a lista de avatares do Room estiver carregada, pega as informações do Realtime
        // específicas para o usuário (retorna uma lista com os IDs dos drawables dos avatares e
        // um booleano associado a cada um para indicar se o usuário já comprou ou não aquele avatar
        avatarViewModel.listAvatarsRoom.observe(this) {
            avatarViewModel.retrieveUserAvatarData(Firebase.auth.currentUser?.email ?: "")
        }
        // Quando a lista de avatares do Realtime específica para o usuário for carregada, cria uma
        // nova lista juntando as informações do Room e do Realtime e retornando um List<Avatar>
        avatarViewModel.listAvatarsRealtime.observe(this) {
            avatarViewModel.mergeAvatarDataRoomRealtime()
        }
        // Quando a lista final for carregada, faz o setup do adapter do RecyclerView
        avatarViewModel.listAvatars.observe(this) {
            adapter =
                AvatarAdapter(
                    this,
                    it,
                    tvTotal,
                    buyAvatarView,
                    buyAvatarDialog,
                    avatarViewModel,
                    realtimeViewModel
                )
            rvAvatar.adapter = adapter
        }

        //configura avatar ativo
        val circularProgressDrawable = CircularProgressDrawable(this)
                circularProgressDrawable.strokeWidth =
                    15f / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                circularProgressDrawable.centerRadius =
                    70f / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                circularProgressDrawable.setColorFilter(
                    ContextCompat.getColor(this, R.color.teal_200),
                    PorterDuff.Mode.SRC_IN
                )
                circularProgressDrawable.start()
        realtimeViewModel.activeUser.observe(this) {
            Glide.with(this).asBitmap()
                .load(it.avatar)
                .placeholder(circularProgressDrawable)
                .into(ivAvatar)
        }

        setUpMenuBehavior()
    }

    override fun onBackPressed() {
        finish()
    }
}