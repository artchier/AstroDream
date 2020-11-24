package com.example.astrodream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SoftwaresAdapter(private val listSoftwares: ArrayList<Software>) : RecyclerView.Adapter<SoftwaresAdapter.SoftwareViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoftwareViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_software, parent, false)
        return SoftwareViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SoftwareViewHolder, position: Int) {
        val software = listSoftwares[position]
        holder.ivSoftware.setImageResource(software.imgSoftware)
        holder.tvCodReferenceSoftware.text = software.codReferenceSoftware
        holder.tvTitleSoftware.text = software.titleSoftware
    }

    override fun getItemCount() = listSoftwares.size

    class SoftwareViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var ivSoftware: ImageView = itemView.findViewById(R.id.ivSoftware)
        var tvCodReferenceSoftware: TextView = itemView.findViewById(R.id.tvCodReferenceSoftware)
        var tvTitleSoftware: TextView = itemView.findViewById(R.id.tvTitleSoftware)
    }
}