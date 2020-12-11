package com.example.astrodream.ui.tech.spinoffs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R

class SpinoffsAdapter(var spinoffListener: OnClickSpinoffListener) :
    RecyclerView.Adapter<SpinoffsAdapter.SpinoffViewHolder>() {

    private val spinoffs = arrayListOf<List<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinoffViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_spinoff, parent, false)
        return SpinoffViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpinoffViewHolder, position: Int) {
        val spinoff = spinoffs[position]

        holder.ivSpinoff.setImageResource(R.drawable.ic_tecnologia)
        holder.tvCodReferenceSpinoff.text = spinoff[1]
        holder.tvTitleSpinoff.text = spinoff[2]
    }

    override fun getItemCount() = spinoffs.size

    fun addSpinoff(s: List<List<String>>) {
        spinoffs.addAll(s)
        notifyDataSetChanged()
    }

    fun getSpinoffs(): ArrayList<List<String>> {
        return spinoffs
    }

    interface OnClickSpinoffListener {
        fun onClickSpinoff(position: Int)
    }

    inner class SpinoffViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivSpinoff: ImageView = itemView.findViewById(R.id.ivSpinoff)
        var tvCodReferenceSpinoff: TextView = itemView.findViewById(R.id.tvCodReferenceSpinoff)
        var tvTitleSpinoff: TextView = itemView.findViewById(R.id.tvTitleSpinoff)

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
}