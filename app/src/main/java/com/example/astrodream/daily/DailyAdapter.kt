package com.example.astrodream.daily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.mars.MarsHistoryAdapter
import com.example.astrodream.mars.MarsPost
import kotlinx.android.synthetic.main.item_daily.view.*
import kotlinx.android.synthetic.main.item_mars_history.view.*

class DailyAdapter(
    private val listDailyPics: ArrayList<Daily>,
    val listener: OnClickDailyListener
) : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    interface OnClickDailyListener {
        fun onClickDaily(position: Int)
    }

    // Classe interna. Essa classe vai inflar o layout do item_mars_history.xml através do onCreateViewHolder
    // O inner pega tudo da classe pai (nesse caso, queremos acessar o listener do construtor do MarsHistoryAdapter)
    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var ivDaily: ImageView = itemView.ivDaily
        var tvDaily: TextView = itemView.tvDaily

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition // posição do item que será clicado
            // esse if garante que estamos clicando em um item existente no RecyclerView, pois pode acontecer de a parte grafica não atualizar e o usuario vê um item que não está mais lá e clica nele
            if (position != RecyclerView.NO_POSITION) {
                listener.onClickDaily(position)
            }
        }
    }

    // Metodos obrigatorios
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false)
        return DailyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val daily: Daily = listDailyPics.get(position)
        // Pega a primeira imagem do post e coloca na ImageView
        Glide.with(holder.itemView).asBitmap()
            .load(daily.img)
            .into(holder.ivDaily)
        // Preenche o TextView com a data do post
        holder.tvDaily.text = daily.date
    }

    override fun getItemCount() = listDailyPics.size
}