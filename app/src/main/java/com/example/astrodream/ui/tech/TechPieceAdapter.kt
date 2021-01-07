package com.example.astrodream.ui.tech

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R

class TechPieceAdapter(
    var onTechPieceClicked: (position: Int) -> Unit,
    val context: Context
) : RecyclerView.Adapter<TechPieceAdapter.TechPieceViewHolder>() {

    private val techPieces = arrayListOf<List<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechPieceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_tech_piece, parent, false)
        return TechPieceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TechPieceViewHolder, position: Int) {
        val techPiece = techPieces[position]

        if (techPiece[10] != "") {
            Glide.with(context).asBitmap()
                .load(techPiece[10])
                .into(holder.ivTechPiece)
        } else {
            holder.ivTechPiece.setImageResource(R.drawable.ic_tecnologia)
        }
        holder.tvCodReferenceTechPiece.text = techPiece[1]
        holder.tvTitleTechPiece.text = techPiece[2]

        holder.itemView.setOnClickListener {
            onTechPieceClicked(position)
        }
    }

    override fun getItemCount() = techPieces.size

    fun addTechPiece(p: List<List<String>>) {
        techPieces.addAll(p)
        notifyDataSetChanged()
    }

    fun getTechPieces(): ArrayList<List<String>> {
        return techPieces
    }

    inner class TechPieceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivTechPiece: ImageView = itemView.findViewById(R.id.ivTechPiece)
        val tvCodReferenceTechPiece: TextView = itemView.findViewById(R.id.tvCodReferenceTechPiece)
        val tvTitleTechPiece: TextView = itemView.findViewById(R.id.tvTitleTechPiece)
    }
}