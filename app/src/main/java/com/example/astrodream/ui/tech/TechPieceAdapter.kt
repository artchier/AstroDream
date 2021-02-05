package com.example.astrodream.ui.tech

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.TranslatorEngToPort
import com.example.astrodream.ui.RealtimeViewModel
import com.example.astrodream.utils.TranslationEnglishToPortuguese
import kotlinx.android.synthetic.main.activity_tech.*

class TechPieceAdapter(
    var onTechPieceClicked: (position: Int) -> Unit,
    val context: Context,
    val realtimeViewModel: RealtimeViewModel,
    val activity: Activity
) : RecyclerView.Adapter<TechPieceAdapter.TechPieceViewHolder>() {

    private val techPieces = arrayListOf<List<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechPieceViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tech_piece, parent, false)
        return TechPieceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TechPieceViewHolder, position: Int) {
        val techPiece = techPieces[position]

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 45f
        circularProgressDrawable.setColorFilter(
            ContextCompat.getColor(this.context, R.color.teal_200),
            PorterDuff.Mode.SRC_IN
        )
        circularProgressDrawable.start()

        if (techPiece[10] != "") {
            Glide.with(context).asBitmap()
                .load(techPiece[10])
                .placeholder(circularProgressDrawable)
                .into(holder.ivTechPiece)
        } else {
            holder.ivTechPiece.setImageResource(R.drawable.ic_tecnologia)
        }

        holder.tvCodReferenceTechPiece.text = techPiece[1]
        TranslatorEngToPort.translateEnglishToPortuguese(techPiece[2], holder.tvTitleTechPiece)

        holder.itemView.setOnClickListener {
            onTechPieceClicked(position)
            realtimeViewModel.animateNasaCoins(
                activity.findViewById(R.id.llNasaCoinsTech),
                activity.findViewById(R.id.tvTotalTech),
                R.string.tecnologias
            )
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

    inner class TechPieceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivTechPiece: ImageView = itemView.findViewById(R.id.ivTechPiece)
        val tvCodReferenceTechPiece: TextView = itemView.findViewById(R.id.tvCodReferenceTechPiece)
        val tvTitleTechPiece: TextView = itemView.findViewById(R.id.tvTitleTechPiece)
    }
}