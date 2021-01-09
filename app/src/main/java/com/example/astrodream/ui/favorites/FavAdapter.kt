package com.example.astrodream.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.domain.Favorite
import com.example.astrodream.domain.PlainClass
import kotlinx.android.synthetic.main.item_fav.view.*

// Essa classe adapter recebe uma lista de favoritos e popula o RecyclerView do FavRecyclerFragment
// Recebe também um listener que quando houver click irá chamar a interface que fará a conexão com a FavRecyclerFragment

class FavAdapter (private val favsList: List<Any>, val listener: OnClickFavListener, val type: String): RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    interface OnClickFavListener {
        fun onClickFav(position: Int)
    }

    // Classe interna. Essa classe vai inflar o layout do item_fav.xml através do onCreateViewHolder
    // O inner pega tudo da classe pai (nesse caso, queremos acessar o listener do construtor do FavAdapter)
    inner class FavViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivFav : ImageView = itemView.ivFav
        var tv1Fav : TextView = itemView.tv1Fav
        var tv2Fav : TextView = itemView.tv2Fav
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition // posição do item que será clicado
            if(position != RecyclerView.NO_POSITION) // esse if garante que estamos clicando em um item existente no RecyclerView, pois pode acontecer de a parte grafica não atualizar e o usuario vê um item que não está mais lá e clica nele
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
                val favorite = favsList[position] as Asteroid
                img = favorite.linkExterno ?: ""
                text1 = favorite.name
                text2 = favorite.close_approach_data ?: ""
            }
            // TODO: implementar uma classe Globe? (usando classe generica Favorite)
            "globe" -> {
                val favorite = favsList[position] as Favorite
                img = favorite.data3
                text1 = favorite.data1
                text2 = favorite.data2
            }
            // TODO: implementar uma classe Tech? (usando classe generica Favorite)
            "tech" -> {
                val favorite = favsList[position] as Favorite
                img = favorite.data3
                text1 = favorite.data1
                text2 = favorite.data2
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