package com.example.astrodream.ui.plaindailymars

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.PlainClass
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.item_detail.view.*

class PlainAdapter(): RecyclerView.Adapter<PlainAdapter.DetailViewHolder>() {

    lateinit var listener: OnClickDetailListener
    lateinit var favListener: OnClickFavListener
    var listHistory = mutableListOf<PlainClass>()
    var emptyItemsCount = 0

    interface OnClickDetailListener {
        fun onClickDetail(position: Int)
    }
    interface OnClickFavListener {
        fun onClickFav(detail:PlainClass, btnFav: ToggleButton)
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

        fun onClickFavorite(detail:PlainClass, btnFav: ToggleButton) {
            favListener.onClickFav(detail, btnFav)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)

        return DetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val shimmerContainer = holder.itemView.shimmer_view_container as ShimmerFrameLayout
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
            holder.itemView.btnFavPlain.isChecked = detail.isFav
            holder.itemView.btnFavPlain.isEnabled = true
            shimmerContainer.stopShimmer()
            shimmerContainer.visibility = View.GONE

            holder.itemView.btnFavPlain.setOnClickListener {
                holder.onClickFavorite(detail, it as ToggleButton)
            }
        }
        else {
            holder.ivDetail.setImageResource(android.R.color.transparent)
            holder.tvDetail.text = ""
            holder.itemView.btnFavPlain.isChecked = false
            holder.itemView.btnFavPlain.isEnabled = false
            shimmerContainer.startShimmer()
            shimmerContainer.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = listHistory.size

    fun replaceItem(item: PlainClass) {
        listHistory[listHistory.size - emptyItemsCount] = item
        notifyItemChanged(listHistory.size - emptyItemsCount)
        emptyItemsCount--
    }

    fun replaceItemAt(item: PlainClass) {
        val pos: Int
        if(item.date != "" && item.earth_date == "") {
            pos = listHistory.indexOfFirst { it.date == item.date }
        } else {
            pos = listHistory.indexOfFirst { it.earth_date == item.earth_date }
        }
        listHistory[pos] = item
        notifyItemChanged(pos)
        Log.i("====ADAPTER===", "$pos ${listHistory[pos]}")
    }

    fun addList(list: MutableList<PlainClass>) {
        emptyItemsCount = list.size
        listHistory.addAll(list)
        notifyDataSetChanged()
    }

}