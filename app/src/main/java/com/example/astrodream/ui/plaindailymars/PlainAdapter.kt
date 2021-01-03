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
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.item_detail.view.*


class PlainAdapter(): RecyclerView.Adapter<PlainAdapter.DetailViewHolder>() {

    lateinit var listener: OnClickDetailListener
    var listHistory = mutableListOf<PlainClass>()
    var emptyItemsCount = 0

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
        val container = holder.itemView.shimmer_view_container as ShimmerFrameLayout
        val detail: PlainClass = listHistory[position]
        val daily = detail.date != "" && detail.earth_date == ""
        val mars = detail.date == "" && detail.earth_date != ""

        val dateRef = if (daily) { detail.date } else { detail.earth_date }
        val imgRef = if (daily) { detail.url } else { detail.img_list[0].img_src }

        if (daily || mars) {
            Glide.with(holder.itemView).asBitmap()
                .load(imgRef)
                .into(holder.ivDetail)
            holder.tvDetail.text = dateRef
            holder.itemView.btnFavPlain.isChecked = false
            holder.itemView.btnFavPlain.isEnabled = true
            container.stopShimmer()
            container.visibility = View.GONE
        }
        else {
            holder.ivDetail.setImageResource(android.R.color.transparent)
            holder.tvDetail.text = ""
            holder.itemView.btnFavPlain.isChecked = false
            holder.itemView.btnFavPlain.isEnabled = false
            container.startShimmer()
            container.visibility = View.VISIBLE
        }

    }

    override fun getItemCount() = listHistory.size

    fun replaceItem(item: PlainClass) {
        listHistory[listHistory.size - emptyItemsCount] = item
        notifyItemChanged(listHistory.size - emptyItemsCount)
        emptyItemsCount--
    }

    fun addList(list: MutableList<PlainClass>) {
        emptyItemsCount = list.size
        listHistory.addAll(list)
        notifyDataSetChanged()
    }

}