package com.example.astrodream.ui.avatar

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.astrodream.R
import com.example.astrodream.entitiesDatabase.Avatar
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_avatar.*

class AvatarActivity : ActivityWithTopBar(R.string.avatar, R.id.dlAvatar) {
    private val avatarViewModel: AvatarViewModel by viewModels()

    /*private val listAvatar: List<Int> = listOf(
        R.drawable.ic_avatar_normal1,
        R.drawable.ic_avatar_normal2,
        R.drawable.ic_avatar_normal3,
        R.drawable.ic_avatar_normal4,
        R.drawable.ic_avatar_normal5,
        R.drawable.ic_avatar_suit,
        R.drawable.ic_avatar_alien,
        R.drawable.ic_avatar_astronaut,
        R.drawable.ic_avatar_naked,
        R.drawable.ic_avatar_nuts
    )*/

    lateinit var adapter: AvatarAdapter
    private lateinit var buyAvatarView: View
    private lateinit var buyAvatarDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        if (getSharedPreferences("first_time", MODE_PRIVATE).getBoolean("avatar", true)) {
            avatarViewModel.addAllAvatarsTask(
                listOf(
                    Avatar(R.drawable.ic_avatar_normal1, 100, "true", "false"),
                    Avatar(R.drawable.ic_avatar_normal2, 100, "true", "false"),
                    Avatar(R.drawable.ic_avatar_normal3, 100, "true", "false"),
                    Avatar(R.drawable.ic_avatar_normal5, 100, "true", "false"),
                    Avatar(R.drawable.ic_avatar_normal4, 200, "true", "false"),
                    Avatar(R.drawable.ic_avatar_suit, 200, "true", "false"),
                    Avatar(R.drawable.ic_avatar_naked, 300, "true", "false"),
                    Avatar(R.drawable.ic_avatar_nuts, 300, "true", "false"),
                    Avatar(R.drawable.ic_avatar_alien, 350, "true", "false"),
                    Avatar(R.drawable.ic_avatar_astronaut, 350, "true", "false")
                )
            )
            getSharedPreferences("first_time", MODE_PRIVATE).edit().putBoolean("avatar", false)
                .apply()
        }

        try {
            ivAvatar.setImageResource(avatarViewModel.lastClickedAvatar.value!!.avatarRes)
        } catch (ignored: Exception) {
        }

        avatarViewModel.getAllAvatarsTask()

        buyAvatarView = View.inflate(this, R.layout.buy_avatar_dialog, null)

        buyAvatarDialog = MaterialAlertDialogBuilder(this)
            .setBackground(ContextCompat.getColor(this, android.R.color.transparent).toDrawable())
            .setView(buyAvatarView)
            .setBackgroundInsetStart(70)
            .setBackgroundInsetEnd(70)
            .setBackgroundInsetTop(10)
            .setBackgroundInsetBottom(100)
            .create()

        avatarViewModel.listAvatars.observe(this) {
            adapter =
                AvatarAdapter(
                    this,
                    it,
                    tvTotal,
                    buyAvatarView,
                    buyAvatarDialog,
                    avatarViewModel
                )
            rvAvatar.adapter = adapter
        }

        avatarViewModel.lastClickedAvatar.observe(this) {
            ivAvatar.setImageResource(it.avatarRes)
        }

        setUpMenuBehavior()
    }

    override fun onBackPressed() {
        finish()
    }
}