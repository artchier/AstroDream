package com.example.astrodream.ui.favorites

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.entitiesDatabase.DailyRoom
import com.example.astrodream.entitiesDatabase.Tech
import com.example.astrodream.entitiesDatabase.MarsRoom
import kotlinx.android.synthetic.main.item_fav.view.*
import java.io.File

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
        var img: String = ""
        var text1 = ""
        var text2 = ""
        when(type) {
            "daily" -> {
                val favRoom = favsList[position] as DailyRoom
                val favorite = PlainClass(title = favRoom.title, date = favRoom.date, url = favRoom.url, explanation = favRoom.explanation)
                img = favorite.url
                text1 = favorite.title
                text2 = favorite.date
            }
            "asteroid" -> {
                val favorite = favsList[position] as Asteroid
                img = favorite.linkExterno ?: ""
                text1 = favorite.name
                text2 = favorite.close_approach_data ?: ""
            }
            "tech" -> {
                val favorite = favsList[position] as Tech

                // img = if(favorite[10] != "") favorite[10]!!.toString() else R.drawable.ic_tecnologia.toString()

                img = "https://lh3.googleusercontent.com/proxy/ntdtUa7YalOxdtRCagcpxwy6r98mY5PUA2e1cITfc9h1PZc0TQ7JS66hl9f5wjVUMeUPf9JR9Gp74vswb2jWmFRqyvO2x8AxliPeRxMnu7ksqRUq4OxWaPvvkE384BENmTPHGqmYeb8j"
                text1 = favorite.typeTech
                text2 = favorite.titleTech
            }
            "mars" -> {
                val favorite = favsList[position] as PlainClass
                img = favorite.img_list[0].img_src
                text1 = "Sol " + favorite.sol.toString()
                text2 = favorite.earth_date
            }
        }

        // Pega a imagem do favorito e coloca na ImageView
        Glide.with(holder.itemView)
            .load(img)
            .into(holder.ivFav)
        // Preenche o primeiro TextView
        holder.tv1Fav.text = text1
        // Preenche o segundo TextView
        holder.tv2Fav.text = text2

        Log.i("===FAVADAPTER==", "${File(img)}")
    }

    override fun getItemCount() = favsList.size
}