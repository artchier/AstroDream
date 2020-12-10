package com.example.astrodream.ui.plaindailymars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.PlainClass
import kotlinx.android.synthetic.main.item_detail.view.*

class PlainAdapter(val listener: OnClickDetailListener): RecyclerView.Adapter<PlainAdapter.DetailViewHolder>() {

    var listHistory = ArrayList<PlainClass>()

    interface OnClickDetailListener {
        fun onClickDetail(position: Int)
    }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivDetail: ImageView = itemView.ivDetail
        var tvDetail: TextView = itemView.tvDetail

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onClickDetail(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return DetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val detail: PlainClass = listHistory[position]
        val imgRef = if (detail.url == "x") { detail.img_list[0] } else { detail.url }
        val dateRef = if (detail.date == "x") { detail.earth_date } else { detail.date }
        Glide.with(holder.itemView).asBitmap()
            .load(imgRef)
            .into(holder.ivDetail)
        holder.tvDetail.text = dateRef
    }

    override fun getItemCount() = listHistory.size

    fun addList(list: List<PlainClass>) {
        listHistory.addAll(list)
        notifyDataSetChanged()
    }
}