package com.example.astrodream.ui.avatar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.PorterDuff
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.Avatar
import com.example.astrodream.entitiesDatabase.AvatarRoom
import com.example.astrodream.ui.RealtimeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.buy_avatar_dialog.view.*

class AvatarAdapter(
    private val context: Context,
    private var listAvatars: List<Avatar>,
    private val tvTotal: TextView,
    private val buyAvatarView: View,
    private val buyAvatarDialog: AlertDialog,
    private val notEnoughCashView: View,
    private val notEnoughCashDialog: AlertDialog,
    private val avatarViewModel: AvatarViewModel,
    private val realtimeUserViewModel: RealtimeViewModel
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    class AvatarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarImageView: ImageView = view.findViewById(R.id.ivCardAvatar)
        val avatarLogo: ImageView = view.findViewById(R.id.ivLogo)
        val avatarSold: ImageView = view.findViewById(R.id.ivSoldAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_item_avatar, parent, false)

        return AvatarViewHolder(view)
    }

    override fun getItemCount() = listAvatars.size

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = listAvatars[position]

        holder.itemView.tag = position
        holder.avatarSold.visibility = INVISIBLE

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth =
            15f / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        circularProgressDrawable.centerRadius =
            70f / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        circularProgressDrawable.setColorFilter(
            ContextCompat.getColor(this.context, R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        circularProgressDrawable.start()

        // Insere a imagem do banco de dados na ImageView através do Glide
        Glide.with(context).asBitmap()
            .load(avatar.avatarRes)
            .placeholder(circularProgressDrawable)
            .into(holder.avatarImageView)

        //verifica se o avatar foi comprado ou não
        if (avatar.isBoughtByCurrentUser && holder.itemView.tag == position) {
            holder.itemView.findViewById<ImageView>(R.id.ivLogo).visibility = INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.tvPrice).visibility = INVISIBLE
            holder.itemView.findViewById<ImageView>(R.id.ivSoldAvatar).visibility = VISIBLE
        } else {
            holder.itemView.findViewById<TextView>(R.id.tvPrice).text = avatar.price.toString()
            holder.itemView.findViewById<ImageView>(R.id.ivLogo).visibility = VISIBLE
            holder.itemView.findViewById<TextView>(R.id.tvPrice).visibility = VISIBLE
        }

        holder.itemView.setOnClickListener {
            if (holder.avatarLogo.visibility != INVISIBLE) {
                buyAvatarView.tvPriceAvatar.text = avatar.price.toString()
                buyAvatarDialog.show()

                buyAvatarView.btnComprar.setOnClickListener {
                    tvTotal.text = realtimeUserViewModel.activeUser.value?.nasaCoins.toString()
                    var total = tvTotal.text.toString().toInt()
                    if (tvTotal.text.toString().toInt() >= buyAvatarView.tvPriceAvatar.text.toString().toInt()) {
                        val animation = ValueAnimator.ofInt(total,
                            total - buyAvatarView.tvPriceAvatar.text.toString().toInt())
                        animation.duration = 500
                        animation.addUpdateListener {
                            tvTotal.text = it.animatedValue.toString()
                        }
                        animation.addListener(object: Animator.AnimatorListener{
                            override fun onAnimationStart(p0: Animator?) {
                            }

                            override fun onAnimationEnd(p0: Animator?) {
                                total -= buyAvatarView.tvPriceAvatar.text.toString().toInt()

                                //atualiza o total de NasaCoins do usuário
                                realtimeUserViewModel.updateUserNasaCoins(
                                    realtimeUserViewModel.activeUser.value?.email!!,
                                    total.toLong()
                                )
                            }

                            override fun onAnimationCancel(p0: Animator?) {}

                            override fun onAnimationRepeat(p0: Animator?) {
                            }

                        })
                        animation.start()

                        //atualiza no realtime e na lista do adapter o último avatar clicado
                        val newAvatar = avatar.avatarRes
                        // Atualiza na lista de avatares do usuário para indicar que esse avatar foi comprado
                        realtimeUserViewModel.updateUserListOfAvatar(
                            realtimeUserViewModel.activeUser.value!!.email,
                            mapOf(newAvatar.toString() to true)
                        )
                        // Atualiza o avatar atual
                        realtimeUserViewModel.updateUserAvatar(
                            realtimeUserViewModel.activeUser.value!!.email,
                            newAvatar.toLong()
                        )
                        // Atualiza a lista no ViewModel e no Recycler
                        avatarViewModel.mergeAvatarDataRoomRealtime()
                        listAvatars = avatarViewModel.listAvatars.value!!

                        notifyDataSetChanged()
                        buyAvatarDialog.dismiss()
                    }
                    else{
                        buyAvatarDialog.dismiss()
                        notEnoughCashDialog.show()
                    }
                }

                buyAvatarView.btnCancelar.setOnClickListener {
                    buyAvatarDialog.dismiss()
                }
            }
            //mostra o avatar já comprado
            else {
                val newAvatar = avatar.avatarRes.toLong()
                realtimeUserViewModel.updateUserAvatar(
                    realtimeUserViewModel.activeUser.value!!.email,
                    newAvatar
                )
            }
        }
    }
}