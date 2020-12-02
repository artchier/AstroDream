package com.example.astrodream.ui.avatar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R
import kotlinx.android.synthetic.main.buy_avatar_dialog.view.*

class AvatarAdapter(
    private val context: Context,
    private val listAvatars: List<Int>,
    private val ivAvatar: ImageView,
    private val tvTotal: TextView,
    private val dialog: View,
    private val buyAvatarDialog: AlertDialog
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

            dialog.btnComprar.setOnClickListener {
                val total =
                    tvTotal.text.toString().toInt() - dialog.tvPriceAvatar.text.toString().toInt()
                tvTotal.text = total.toString()
                ivAvatar.setImageResource(avatar)
                buyAvatarDialog.dismiss()
            }

            dialog.btnCancelar.setOnClickListener {
                buyAvatarDialog.dismiss()
            }

            buyAvatarDialog.show()
        }
    }

    override fun getItemCount() = listAvatars.size
}