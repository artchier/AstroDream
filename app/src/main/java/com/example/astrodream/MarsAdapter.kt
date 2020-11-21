package com.example.astrodream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_mars_post.view.*

class MarsAdapter (private val context: Context, private val MarsPicsList: ArrayList<String>) : PagerAdapter() {

    override fun getCount() = MarsPicsList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    // Essa função infla o layout e já repete os itens
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = LayoutInflater.from(context).inflate(R.layout.card_mars_post, container, false)
        var marsPost = MarsPicsList.get(position)

        Picasso.get().load(marsPost).into(view.ivRecentMars)

        container.addView(view)

        return view
    }

    // Remove as views que não estão sendo mais utilizadas
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}