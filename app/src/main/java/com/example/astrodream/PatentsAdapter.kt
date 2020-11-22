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
        var patent = listPatents[position]
        holder.ivPatent.setImageResource(patent.img)
        holder.tvCodReference.text = patent.codReference
        holder.tvTitlePatent.text = patent.titlePatent
    }

    override fun getItemCount() = listPatents.size

    class PatentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var ivPatent: ImageView = itemView.findViewById(R.id.ivPatent)
        var tvCodReference: TextView = itemView.findViewById(R.id.tvCodReference)
        var tvTitlePatent: TextView = itemView.findViewById(R.id.tvTitlePatent)
    }
}