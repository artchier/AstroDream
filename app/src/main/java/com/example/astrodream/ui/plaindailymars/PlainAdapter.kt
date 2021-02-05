package com.example.astrodream.ui.plaindailymars

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.services.buildDownloadSetWallpaperMenu
import com.example.astrodream.ui.mars.RecentMarsFragment
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.item_detail.view.*

class PlainAdapter: RecyclerView.Adapter<PlainAdapter.DetailViewHolder>() {

    lateinit var listener: OnClickDetailListener
    lateinit var favListener: OnClickFavListener
    lateinit var context: Context
    var listHistory = mutableListOf<PlainClass>()
    var emptyItemsCount = 0

    interface OnClickDetailListener {
        fun onClickDetail(position: Int)
    }
    interface OnClickFavListener {
        fun onClickFav(detail:PlainClass, btnFav: ToggleButton)
    }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val ivDetail: ImageView = itemView.ivDetail
        val tvDetail: TextView = itemView.tvDetail
        val btnDownloadWallpaper: ImageButton = itemView.btnDownloadWallpaper

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onClickDetail(position)
                RecentMarsFragment.hasClicked = true
            }
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
        val hdImgRef = if (daily) { detail.hdurl } else { imgRef }

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
                favListener.onClickFav(detail, it as ToggleButton)
            }

            holder.btnDownloadWallpaper.setOnClickListener {
                buildDownloadSetWallpaperMenu(context, holder.btnDownloadWallpaper, hdImgRef)
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
        val pos: Int = if(item.date != "" && item.earth_date == "") {
            listHistory.indexOfFirst { it.date == item.date }
        } else {
            listHistory.indexOfFirst { it.earth_date == item.earth_date }
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