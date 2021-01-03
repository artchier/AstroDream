package com.example.astrodream.ui.mars

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.astrodream.R
import com.example.astrodream.domain.MarsImage
import com.example.astrodream.ui.FullScreenImgActivity
import kotlinx.android.synthetic.main.card_mars_post.view.*

// Esse adapter é responsável por inflar os layouts das paginas do
// ViewPager (dentro da RecentMarsFragment) com as imagens do dia selecionado
class MarsAdapter(
    private val context: Context,
    private val marsPicsList: List<MarsImage>,
    private val postDate: String,
    private val postSol: String,
    private val maxTemp: String,
    private val minTemp: String
) : PagerAdapter() {

    override fun getCount() = marsPicsList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    // Essa função infla o layout e já repete os itens
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.card_mars_post, container, false)
        val marsPic = marsPicsList[position].img_src
        val marsCamera = marsPicsList[position].camera.full_name

        val circularProgressDrawable = CircularProgressDrawable(container.context)
        circularProgressDrawable.strokeWidth = 15f
        circularProgressDrawable.centerRadius = 80f
        circularProgressDrawable.setColorFilter(
            getColor(this.context, R.color.teal_200),
            PorterDuff.Mode.SRC_IN
        )
        circularProgressDrawable.start()

        // Insere a imagem da URL na ImageView através do Glide
        Glide.with(view).asBitmap()
            .load(marsPic)
            .placeholder(circularProgressDrawable)
            .into(view.ivRecentMars)
        // Atualiza as TextViews com os dados do post
        view.tvMarsRecentDateEarth.text = postDate
        view.tvMarsRecentDateSol.text = postSol
        view.tvMarsRecentMaxTemp.text = maxTemp
        view.tvMarsRecentMinTemp.text = minTemp
        view.tvMarsRecentCamera.text = marsCamera

        view.ivRecentMars.setOnClickListener {
            if (marsPic is String) { // marcPic is String when API request is successful
                val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
                intent.putExtra("img", marsPic)
                startActivity(container.context, intent, null)
            }
        }

        container.addView(view)

        return view
    }

    // Remove as views que não estão sendo mais utilizadas
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}