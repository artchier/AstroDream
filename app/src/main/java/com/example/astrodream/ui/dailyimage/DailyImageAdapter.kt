package com.example.astrodream.ui.dailyimage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.DailyImage
import kotlinx.android.synthetic.main.item_daily.view.*

class DailyImageAdapter(
    /*private val listDailyPics: ArrayList<DailyImage>,*/
    val listener: OnClickDailyListener
) : RecyclerView.Adapter<DailyImageAdapter.DailyViewHolder>() {

    var listDailyPics = ArrayList<DailyImage>()

    interface OnClickDailyListener {
        fun onClickDaily(position: Int)
    }

    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var ivDaily: ImageView = itemView.ivDaily
        var tvDaily: TextView = itemView.tvDaily

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onClickDaily(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false)
        return DailyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val dailyImage: DailyImage = listDailyPics[position]
        Glide.with(holder.itemView).asBitmap()
            .load(dailyImage.url)
            .into(holder.ivDaily)
        holder.tvDaily.text = dailyImage.date
    }

    override fun getItemCount() = listDailyPics.size

    fun addList(list: List<DailyImage>) {
        listDailyPics.addAll(list)
        notifyDataSetChanged()
    }
}