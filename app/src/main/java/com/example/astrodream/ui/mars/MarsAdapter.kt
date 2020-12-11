package com.example.astrodream.ui.mars

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.astrodream.R
import kotlinx.android.synthetic.main.card_mars_post.view.*

// Esse adapter é responsável por inflar os layouts das paginas do
// ViewPager (dentro da RecentMarsFragment) com as imagens do dia selecionado
class MarsAdapter(
    private val context: Context,
    private val MarsPicsList: List<String>,
    private val postDate: String,
    private val postSol: String,
    private val maxTemp: String,
    private val minTemp: String
) : PagerAdapter() {

    override fun getCount() = MarsPicsList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    // Essa função infla o layout e já repete os itens
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.card_mars_post, container, false)
        val marsPost = MarsPicsList[position]
        // Insere a imagem da URL na ImageView através do Glide
        Glide.with(view).asBitmap()
            .load(marsPost)
            .into(view.ivRecentMars)
        // Atualiza as TextViews com os dados do post
        view.tvMarsRecentDateEarth.text = postDate
        view.tvMarsRecentDateSol.text = postSol
        view.tvMarsRecentMaxTemp.text = maxTemp
        view.tvMarsRecentMinTemp.text = minTemp

        container.addView(view)

        return view
    }

    // Remove as views que não estão sendo mais utilizadas
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}