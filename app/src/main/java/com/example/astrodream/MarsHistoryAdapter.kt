package com.example.astrodream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_mars_history.view.*

// Essa classe adapter recebe uma lista e popula o RecyclerView
// Recebe também um listener que quando houver click irá chamar a interface que fará a conexão com a main activity
class MarsHistoryAdapter (private val MarsPostsList: ArrayList<MarsPost>, val listener: OnClickMarsPostListener): RecyclerView.Adapter<MarsHistoryAdapter.MarsHistoryViewHolder>() {
    interface OnClickMarsPostListener {
        fun onClickMarsPost(position: Int)
    }

    // Classe interna. Essa classe vai inflar o layout do item_mars_history.xml através do onCreateViewHolder
    // O inner pega tudo da classe pai (nesse caso, queremos acessar o listener do construtor do MarsHistoryAdapter)
    inner class MarsHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivMarsPost : ImageView = itemView.ivMarsPost
        var tvMarsPost : TextView = itemView.tvMarsPost
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition // posição do item que será clicado -> "automatico"
            if(position != RecyclerView.NO_POSITION) // esse if garante que estamos clicando em um item existente no RecyclerView, pois pode acontecer de a parte grafica não atualizar e o usuario vê um item que não está mais lá e clica nele
                listener.onClickMarsPost(position)
        }
    }

    // Metodos obrigatorios
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mars_history, parent, false)
        return MarsHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MarsHistoryViewHolder, position: Int) {
        var marsPost: MarsPost = MarsPostsList.get(position)
        // Pega a primeira imagem do posto e coloca na ImageView
        Picasso.get().load(marsPost.img_list[0]).into(holder.ivMarsPost)
        // Preenche o TextView com a data do post
        holder.tvMarsPost.text = marsPost.earth_date
    }

    override fun getItemCount() = MarsPostsList.size
}