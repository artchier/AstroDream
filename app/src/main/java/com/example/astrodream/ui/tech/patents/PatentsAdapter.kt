package com.example.astrodream.ui.tech.patents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R

class PatentsAdapter(var patentListener: OnClickPatentListener, val context: Context) :
    RecyclerView.Adapter<PatentsAdapter.PatentViewHolder>() {

    private val patents = arrayListOf<List<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_patent, parent, false)
        return  PatentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PatentViewHolder, position: Int) {
        val patent = patents[position]

        holder.ivPatent.setImageResource(R.drawable.ic_tecnologia)
        holder.tvCodReferencePatent.text = patent[1]
        holder.tvTitlePatent.text = patent[2]
        holder.tvDescPatent.text = patent[3]
    }

    override fun getItemCount() = patents.size

    fun addPatent(p: List<List<String>>) {
        patents.addAll(p)
        notifyDataSetChanged()
    }

    interface OnClickPatentListener {
        fun onClickPatent(position: Int)
    }

    inner class PatentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivPatent: ImageView = itemView.findViewById(R.id.ivPatent)
        var tvCodReferencePatent: TextView = itemView.findViewById(R.id.tvCodReferencePatent)
        var tvTitlePatent: TextView = itemView.findViewById(R.id.tvTitlePatent)
        var tvDescPatent: TextView = itemView.findViewById(R.id.tvDescPatent)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                patentListener.onClickPatent(position)
            }
        }
    }
}