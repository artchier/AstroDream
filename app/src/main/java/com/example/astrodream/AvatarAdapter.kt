package com.example.astrodream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class AvatarAdapter (private val context: Context, private val listAvatars: List<Int>): RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    class AvatarViewHolder(view: View): RecyclerView.ViewHolder(view){
        val avatarImageView: ImageView = view.findViewById(R.id.ivCardAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item_avatar, parent, false)

        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = listAvatars[position]

        holder.avatarImageView.setImageResource(avatar)
    }

    override fun getItemCount() = listAvatars.size
}