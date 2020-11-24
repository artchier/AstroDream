package com.example.astrodream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class SoftwaresAdapter(private val listSoftwares: ArrayList<Software>, var softwareListener: OnClickSoftwareListener)
    : RecyclerView.Adapter<SoftwaresAdapter.SoftwareViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoftwareViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_software, parent, false)
        return SoftwareViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SoftwareViewHolder, position: Int) {
        val software = listSoftwares[position]
        holder.ivSoftware.setImageResource(software.imgSoftware)
        holder.tvCodReferenceSoftware.text = software.codReferenceSoftware
        holder.tvTitleSoftware.text = software.titleSoftware
        holder.tbDescSoftware.text = software.descSoftware
    }

    override fun getItemCount() = listSoftwares.size

    inner class SoftwareViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivSoftware: ImageView = itemView.findViewById(R.id.ivSoftware)
        var tvCodReferenceSoftware: TextView = itemView.findViewById(R.id.tvCodReferenceSoftware)
        var tvTitleSoftware: TextView = itemView.findViewById(R.id.tvTitleSoftware)
        var tbDescSoftware: TextView = itemView.findViewById(R.id.tvDescSoftware)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                softwareListener.onClickSoftware(position)
            }
        }
    }

    interface OnClickSoftwareListener {
        fun onClickSoftware(position: Int)
    }
}