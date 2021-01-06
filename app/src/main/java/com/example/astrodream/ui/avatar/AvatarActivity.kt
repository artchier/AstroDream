package com.example.astrodream.ui.avatar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.astrodream.R
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_avatar.*

class AvatarActivity : ActivityWithTopBar(R.string.avatar, R.id.dlAvatar) {

    private val listAvatar: List<Int> = listOf(
            R.drawable.ic_avatar_normal1,
            R.drawable.ic_avatar_normal2,
            R.drawable.ic_avatar_normal3,
            R.drawable.ic_avatar_normal4,
            R.drawable.ic_avatar_normal5,
            R.drawable.ic_avatar_alien,
            R.drawable.ic_avatar_astronaut,
            R.drawable.ic_avatar_naked,
            R.drawable.ic_avatar_nuts,
            R.drawable.ic_avatar_suit
    )
    lateinit var adapter: AvatarAdapter
    private lateinit var buyAvatarView: View
    private lateinit var buyAvatarDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        buyAvatarView = View.inflate(this, R.layout.buy_avatar_dialog, null)

        buyAvatarDialog = MaterialAlertDialogBuilder(this)
            .setBackground(ContextCompat.getColor(this, android.R.color.transparent).toDrawable())
            .setView(buyAvatarView)
            .setBackgroundInsetStart(70)
            .setBackgroundInsetEnd(70)
            .setBackgroundInsetTop(10)
            .setBackgroundInsetBottom(100)
            .create()

        adapter = AvatarAdapter(this, listAvatar, ivAvatar, tvTotal, buyAvatarView, buyAvatarDialog)

        rvAvatar.adapter = adapter

        setUpMenuBehavior()
    }

    override fun onBackPressed() {
        finish()
    }
}