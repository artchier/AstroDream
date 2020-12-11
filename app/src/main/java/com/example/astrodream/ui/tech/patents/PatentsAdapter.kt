package com.example.astrodream.ui.tech.patents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        if (patent[10] != "") {
            Glide.with(context).asBitmap()
                .load(patent[10])
                .into(holder.ivPatent)
        } else {
            holder.ivPatent.setImageResource(R.drawable.ic_tecnologia)
        }
        holder.tvCodReferencePatent.text = patent[1]
        holder.tvTitlePatent.text = patent[2]
    }

    override fun getItemCount() = patents.size

    fun addPatent(p: List<List<String>>) {
        patents.addAll(p)
        notifyDataSetChanged()
    }

    fun getPatents(): ArrayList<List<String>> {
        return patents
    }

    inner class PatentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivPatent: ImageView = itemView.findViewById(R.id.ivPatent)
        var tvCodReferencePatent: TextView = itemView.findViewById(R.id.tvCodReferencePatent)
        var tvTitlePatent: TextView = itemView.findViewById(R.id.tvTitlePatent)

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

    interface OnClickPatentListener {
        fun onClickPatent(position: Int)
    }
}