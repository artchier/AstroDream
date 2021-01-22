package com.example.astrodream.ui.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.entitiesDatabase.AsteroidRoom
import kotlinx.android.synthetic.main.item_fav.view.*

class FavAdapter(
    private val favsList: List<Any>,
    val listener: OnClickFavListener,
    val type: String
): RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    interface OnClickFavListener {
        fun onClickFav(position: Int)
    }

    inner class FavViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivFav : ImageView = itemView.ivFav
        var tv1Fav : TextView = itemView.tv1Fav
        var tv2Fav : TextView = itemView.tv2Fav
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition // posição do item que será clicado
            if(position != RecyclerView.NO_POSITION)
                listener.onClickFav(position)
        }
    }

    // Metodos obrigatorios
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_fav, parent, false)
        return FavViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        var img: Any = ""
        var text1 = ""
        var text2 = ""
        when(type) {
            "daily" -> {
                val favorite = favsList[position] as PlainClass
                img = favorite.url
                text1 = favorite.title
                text2 = favorite.date
            }
            "asteroid" -> {
                val favorite = favsList[position] as AsteroidRoom
                img = R.drawable.asteroide
                text1 = favorite.codeAsteroid
                text2 = ""
            }
            // TODO: implementar uma classe Tech para saber se é patente, software ou spinoff
            "tech" -> {
                val favorite = favsList[position] as List<*>
                img = if(favorite[10] != "") {
                    favorite[10]!!
                } else {
                    R.drawable.ic_tecnologia
                }
                text1 = "Patente"
                text2 = favorite[2] as String
            }
            "mars" -> {
                val favorite = favsList[position] as PlainClass
                img = favorite.img_list[0].img_src
                text1 = "Sol " + favorite.sol.toString()
                text2 = favorite.earth_date
            }
        }

        // Pega a imagem do favorito e coloca na ImageView
        Glide.with(holder.itemView).asBitmap()
            .load(img)
            .into(holder.ivFav)
        // Preenche o primeiro TextView
        holder.tv1Fav.text = text1
        // Preenche o segundo TextView
        holder.tv2Fav.text = text2
    }

    override fun getItemCount() = favsList.size
}