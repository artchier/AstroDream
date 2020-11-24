package com.example.astrodream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PatentsAdapter(private val listPatents: ArrayList<Patent>, var patentListener: OnClickPatentListener, val context: Context) :
    RecyclerView.Adapter<PatentsAdapter.PatentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_patent, parent, false)
        return  PatentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PatentViewHolder, position: Int) {
        val patent = listPatents[position]

        //Picasso.with(context).load("https://technology.nasa.gov/t2media/tops/img/LEW-TOPS-125/iStock-157730835_LEW-19309-1_airplane-storm_1388x1050-300dpi.jpg").fit().into(holder.ivPatent)
        holder.ivPatent.setImageResource(patent.imgPatent)
        holder.tvCodReferencePatent.text = patent.codReferencePatent
        holder.tvTitlePatent.text = patent.titlePatent
        holder.tvDescPatent.text = patent.descPatent
    }

    override fun getItemCount() = listPatents.size

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

    interface OnClickPatentListener {
        fun onClickPatent(position: Int)
    }
}