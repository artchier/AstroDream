package com.example.astrodream.ui.globe

import android.content.Context
import android.graphics.PorterDuff
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.services.buildGlobeImageUrl
import kotlinx.android.synthetic.main.card_globe.view.*
import java.util.*


class GlobeAdapter(
    private val epicImageList: List<String>,
    private val context: Context,
    private val date: String
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun getCount() = epicImageList.size

    // Essa função infla o layout e já repete os itens
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.card_globe, container, false)

        val circularProgressDrawable = CircularProgressDrawable(container.context)
        circularProgressDrawable.strokeWidth =
            15f / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        circularProgressDrawable.centerRadius =
            70f / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        circularProgressDrawable.setColorFilter(
            ContextCompat.getColor(this.context, R.color.teal_200),
            PorterDuff.Mode.SRC_IN
        )
        circularProgressDrawable.start()

        // Insere a imagem da URL na ImageView através do Glide
        try {
            Glide.with(view).asBitmap()
                .load(buildGlobeImageUrl(date, epicImageList[position]))
                .placeholder(circularProgressDrawable)
                .into(view.ivGlobe)
        } catch (exception: Exception) {

        }
        container.addView(view)

        return view
    }

    // Remove as views que não estão sendo mais utilizadas
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}