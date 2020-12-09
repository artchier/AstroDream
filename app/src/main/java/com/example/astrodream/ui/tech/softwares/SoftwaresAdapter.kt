package com.example.astrodream.ui.tech.softwares

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R

class SoftwaresAdapter(var softwareListener: OnClickSoftwareListener)
    : RecyclerView.Adapter<SoftwaresAdapter.SoftwareViewHolder>() {

    private val softwares = arrayListOf<List<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoftwareViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_software, parent, false)
        return SoftwareViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SoftwareViewHolder, position: Int) {
        val software = softwares[position]

        holder.ivSoftware.setImageResource(R.drawable.ic_tecnologia)
        holder.tvCodReferenceSoftware.text = software[1]
        holder.tvTitleSoftware.text = software[2]
        holder.tbDescSoftware.text = software[3]
    }

    override fun getItemCount() = softwares.size

    fun addSoftware(s: List<List<String>>) {
        softwares.addAll(s)
        notifyDataSetChanged()
    }

    interface OnClickSoftwareListener {
        fun onClickSoftware(position: Int)
    }

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
}