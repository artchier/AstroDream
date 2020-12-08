package com.example.astrodream.ui.mars

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.astrodream.R
import com.example.astrodream.ui.dailyimage.DailyImageFragment
import com.example.astrodream.ui.plaindailymars.PlainDetailFragment
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*
import me.relex.circleindicator.CircleIndicator

class RecentMarsFragment : PlainDetailFragment(R.layout.fragment_recent_mars) {

    companion object {
        fun newInstance() = RecentMarsFragment()
    }

    lateinit var adapterMars: MarsAdapter

    override fun popView(view: View) {
        adapterMars = MarsAdapter(
            view.context,
            plainDetail.img_list,
            plainDetail.earth_date,
            plainDetail.maxTemp,
            plainDetail.minTemp
        )

        // Atribui o adapter criado acima ao adapter do ViewPager
        view.vpMarsRecent.adapter = adapterMars

        // Inclui o indicador de bolinhas
        val indicator = view.ciMarsRecent as CircleIndicator
        indicator.setViewPager(view.vpMarsRecent)

        // Acerta o texto acima da imagem para mostrar o dia do post
        view.postDescr.text = plainDetail.earth_date

    }

}