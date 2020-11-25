package com.example.astrodream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SpinoffsAdapter(private val listSpinoffs: ArrayList<Spinoff>, var spinoffListener: OnClickSpinoffListener) :
    RecyclerView.Adapter<SpinoffsAdapter.SpinoffViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinoffViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_spinoff, parent, false)
        return SpinoffViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpinoffViewHolder, position: Int) {
        val spinoff = listSpinoffs[position]
        holder.ivSpinoff.setImageResource(spinoff.imgSpinoff)
        holder.tvCodReferenceSpinoff.text = spinoff.codReferenceSpinoff
        holder.tvTitleSpinoff.text = spinoff.titleSpinoff
        holder.tvDescSpinoff.text = spinoff.descSpinoff
    }

    override fun getItemCount() = listSpinoffs.size

    inner class SpinoffViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivSpinoff: ImageView = itemView.findViewById(R.id.ivSpinoff)
        var tvCodReferenceSpinoff: TextView = itemView.findViewById(R.id.tvCodReferenceSpinoff)
        var tvTitleSpinoff: TextView = itemView.findViewById(R.id.tvTitleSpinoff)
        var tvDescSpinoff: TextView = itemView.findViewById(R.id.tvDescSpinoff)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                spinoffListener.onClickSpinoff(position)
            }
        }
    }

    interface OnClickSpinoffListener {
        fun onClickSpinoff(position: Int)
    }
}