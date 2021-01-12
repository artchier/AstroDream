package com.example.astrodream.ui.dailyimage

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.R
import com.example.astrodream.ui.plaindailymars.PlainDetailFragment
import kotlinx.android.synthetic.main.fragment_daily.*

class DailyImageFragment : PlainDetailFragment(R.layout.fragment_daily) {

    companion object {
        fun newInstance() = DailyImageFragment()
    }

    override fun popView(view: View) {
        tvTitle.text = plainDetail.title
        Glide.with(view).asBitmap()
            .load(plainDetail.url)
            .into(cvDaily)
        tvDate.text = plainDetail.date

        cvDaily.setOnClickListener {
            val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
            intent.putExtra("img", plainDetail.url)
            startActivity(intent)
        }
    }

}