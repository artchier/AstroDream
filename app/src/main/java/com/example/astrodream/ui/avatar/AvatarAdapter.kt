package com.example.astrodream.ui.avatar

import android.content.Context
import android.graphics.PorterDuff
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.entitiesDatabase.Avatar
import com.example.astrodream.services.buildGlobeImageUrl
import kotlinx.android.synthetic.main.buy_avatar_dialog.view.*
import kotlinx.android.synthetic.main.card_globe.view.*

class AvatarAdapter(
    private val context: Context,
    private val listAvatars: List<Avatar>,
    private val tvTotal: TextView,
    private val buyAvatarView: View,
    private val buyAvatarDialog: AlertDialog,
    private val avatarViewModel: AvatarViewModel
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    lateinit var animation: AlphaAnimation

    class AvatarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarImageView: ImageView = view.findViewById(R.id.ivCardAvatar)
        val avatarLogo: ImageView = view.findViewById(R.id.ivLogo)
        val avatarPrice: TextView = view.findViewById(R.id.tvPrice)
        val avatarSold: ImageView = view.findViewById(R.id.ivSoldAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_item_avatar, parent, false)

        animation = AlphaAnimation(0f, 1f)
        animation.duration = 300
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = listAvatars[position]

        holder.avatarSold.visibility = INVISIBLE

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth =
            15f / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        circularProgressDrawable.centerRadius =
            70f / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        circularProgressDrawable.setColorFilter(
            ContextCompat.getColor(this.context, R.color.teal_200),
            PorterDuff.Mode.SRC_IN
        )
        circularProgressDrawable.start()

        // Insere a imagem do banco de dados na ImageView através do Glide
        Glide.with(context).asBitmap()
            .load(avatar.avatarRes)
            .placeholder(circularProgressDrawable)
            .into(holder.avatarImageView)

        if (avatar.isAvailableToBuy == "false") {
            holder.avatarLogo.visibility = INVISIBLE
            holder.avatarPrice.visibility = INVISIBLE
            holder.avatarSold.visibility = VISIBLE
        } else {
            holder.avatarPrice.text = avatar.price.toString()
        }

        holder.avatarImageView.setOnClickListener {

            buyAvatarView.tvPriceAvatar.text = avatar.price.toString()
            buyAvatarView.btnComprar.setOnClickListener {
                val total =
                    tvTotal.text.toString().toInt() - buyAvatarView.tvPriceAvatar.text.toString()
                        .toInt()
                tvTotal.text = total.toString()
                holder.avatarLogo.visibility = INVISIBLE
                holder.avatarPrice.visibility = INVISIBLE
                holder.avatarSold.visibility = VISIBLE
                holder.avatarSold.startAnimation(animation)

                val newAvatar = Avatar(avatar.avatarRes, avatar.price, "false", "true")

                if (avatarViewModel.lastClickedAvatar.value == null) {
                    avatarViewModel.lastClickedAvatar.value = newAvatar

                } else {
                    avatarViewModel.updateLastClickedAvatarTask(
                        Avatar(
                            avatarViewModel.lastClickedAvatar.value!!.avatarRes,
                            avatarViewModel.lastClickedAvatar.value!!.price,
                            "false",
                            "false"
                        ),
                        newAvatar
                    )
                    avatarViewModel.lastClickedAvatar.value = newAvatar
                }
                avatarViewModel.buyAvatarTask(newAvatar)
                buyAvatarDialog.dismiss()
            }

            buyAvatarView.btnCancelar.setOnClickListener {
                buyAvatarDialog.dismiss()
            }

            if (holder.avatarLogo.visibility != INVISIBLE)
                buyAvatarDialog.show()
            else {
                avatarViewModel.lastClickedAvatar.value = avatar
            }
        }
    }

    override fun getItemCount() = listAvatars.size
}