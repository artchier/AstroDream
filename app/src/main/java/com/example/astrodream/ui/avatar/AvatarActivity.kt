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
import com.example.astrodream.entitiesDatabase.Avatar
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            avatarViewModel.addAllAvatarsTask()
            getSharedPreferences("first_time", MODE_PRIVATE).edit().putBoolean("avatar", false)
                .apply()
        }

        //seta o total de NasaCoins que o usuário possui
        tvTotal.text = getSharedPreferences("nasaCoins", MODE_PRIVATE).getInt("total", 1000).toString()

        avatarViewModel.getAllAvatarsTask()

        avatarViewModel.getLastClickedAvatarTask()

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

        //verifica se a lista de avatares foi atualizada
        avatarViewModel.listAvatars.observe(this) {
            adapter =
                AvatarAdapter(
                    this,
                    it as MutableList<Avatar>,
                    tvTotal,
                    buyAvatarView,
                    buyAvatarDialog,
                    avatarViewModel
                )
            rvAvatar.adapter = adapter
        }

        //verifica o último avatar clicado
        avatarViewModel.lastClickedAvatar.observe(this) {
            try {
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

                // Insere a imagem do banco de dados na ImageView através do Glide
                Glide.with(this).asBitmap()
                    .load(avatarViewModel.lastClickedAvatar.value!!.avatarRes)
                    .placeholder(circularProgressDrawable)
                    .into(ivAvatar)

            } catch (ignored: Exception) {
            }
        }

        setUpMenuBehavior()
    }

    override fun onBackPressed() {
        finish()
    }
}