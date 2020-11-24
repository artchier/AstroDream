package com.example.astrodream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PatentsAdapter(private val listPatents: ArrayList<Patent>) : RecyclerView.Adapter<PatentsAdapter.PatentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_patent, parent, false)
        return  PatentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PatentViewHolder, position: Int) {
        val patent = listPatents[position]
        holder.ivPatent.setImageResource(patent.imgPatent)
        holder.tvCodReferencePatent.text = patent.codReferencePatent
        holder.tvTitlePatent.text = patent.titlePatent
    }

    override fun getItemCount() = listPatents.size

    class PatentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var ivPatent: ImageView = itemView.findViewById(R.id.ivPatent)
        var tvCodReferencePatent: TextView = itemView.findViewById(R.id.tvCodReferencePatent)
        var tvTitlePatent: TextView = itemView.findViewById(R.id.tvTitlePatent)
    }
}