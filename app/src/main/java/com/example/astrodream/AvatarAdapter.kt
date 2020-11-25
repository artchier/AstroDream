package com.example.astrodream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AvatarAdapter(
    private val context: Context,
    private val listAvatars: List<Int>
) :
    RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    class AvatarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarImageView: ImageView = view.findViewById(R.id.ivCardAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_item_avatar, parent, false)

        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = listAvatars[position]

        holder.avatarImageView.setImageResource(avatar)

        holder.avatarImageView.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setView(R.layout.buy_avatar_dialog)
                .setBackgroundInsetTop(100)
                .setBackgroundInsetBottom(100)
                .setBackgroundInsetStart(100)
                .setBackgroundInsetEnd(100)
                .setBackground(
                    ContextCompat.getColor(context, android.R.color.transparent).toDrawable()
                )
                .show()
        }
    }

    override fun getItemCount() = listAvatars.size
}