package com.example.astrodream.ui.avatar

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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.entitiesDatabase.Avatar
import kotlinx.android.synthetic.main.buy_avatar_dialog.view.*

class AvatarAdapter(
    private val context: Context,
    private val listAvatars: MutableList<Avatar>,
    private val tvTotal: TextView,
    private val buyAvatarView: View,
    private val buyAvatarDialog: AlertDialog,
    private val avatarViewModel: AvatarViewModel
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
            ContextCompat.getColor(this.context, R.color.teal_200),
            PorterDuff.Mode.SRC_IN
        )
        circularProgressDrawable.start()

        // Insere a imagem do banco de dados na ImageView através do Glide
        Glide.with(context).asBitmap()
            .load(avatar.avatarRes)
            .placeholder(circularProgressDrawable)
            .into(holder.avatarImageView)

        //verifica se o avatar foi comprado ou não
        if (avatar.isAvailableToBuy == "false" && holder.itemView.tag == position) {
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
                    tvTotal.text = context.getSharedPreferences("nasaCoins", MODE_PRIVATE)
                        .getInt("total", 1000).toString()
                    val total =
                        tvTotal.text.toString()
                            .toInt() - buyAvatarView.tvPriceAvatar.text.toString()
                            .toInt()
                    tvTotal.text = total.toString()

                    //atualiza o total de NasaCoins do usuário
                    context.getSharedPreferences("nasaCoins", MODE_PRIVATE).edit()
                        .putInt("total", total).apply()

                    val newAvatar = Avatar(avatar.avatarRes, avatar.price, "false", "true")
                    newAvatar.idAvatar = avatar.idAvatar

                    //atualiza no banco de dados e na lista do adapter o último avatar clicado no caso de não haver outro anterior
                    if (avatarViewModel.lastClickedAvatar.value == null) {
                        avatarViewModel.lastClickedAvatar.value = newAvatar
                        listAvatars[newAvatar.idAvatar - 1] = newAvatar
                        notifyDataSetChanged()
                    }
                    //atualiza no banco de dados e na lista do adapter o último avatar clicado e o anterior a ele
                    else {
                        val oldAvatar = Avatar(
                            avatarViewModel.lastClickedAvatar.value!!.avatarRes,
                            avatarViewModel.lastClickedAvatar.value!!.price,
                            "false",
                            "false"
                        )
                        oldAvatar.idAvatar = avatarViewModel.lastClickedAvatar.value!!.idAvatar
                        avatarViewModel.updateLastClickedAvatarTask(
                            newAvatar,
                            oldAvatar
                        )
                        avatarViewModel.lastClickedAvatar.value = newAvatar
                        listAvatars[oldAvatar.idAvatar - 1] = oldAvatar
                        listAvatars[newAvatar.idAvatar - 1] = newAvatar
                        notifyDataSetChanged()
                    }
                    avatarViewModel.buyAvatarTask(newAvatar)
                    buyAvatarDialog.dismiss()
                }

                buyAvatarView.btnCancelar.setOnClickListener {
                    buyAvatarDialog.dismiss()
                }
            }
            //mostra o avatar já comprado
            else {
                avatarViewModel.lastClickedAvatar.value = avatar
            }
        }
    }
}