package com.example.astrodream.ui.mars

import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.fragment.app.activityViewModels
import com.example.astrodream.R
import com.example.astrodream.ui.plaindailymars.PlainDetailFragment
import com.example.astrodream.ui.plaindailymars.PlainViewModel
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*
import me.relex.circleindicator.CircleIndicator
import com.example.astrodream.entitiesDatabase.MarsRoom

class RecentMarsFragment : PlainDetailFragment(R.layout.fragment_recent_mars) {

    companion object {
        fun newInstance() = RecentMarsFragment()
    }

    lateinit var adapterMars: MarsAdapter
    val viewModel: PlainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.btnFavMars.setOnClickListener{
            viewModel.favMarsDB(MarsRoom(plainDetail.earth_date, plainDetail.sol, plainDetail.maxTemp, plainDetail.minTemp),
                it as ToggleButton
            )
        }
    }

    override fun popView(view: View) {

        val contextActivity = this.requireActivity()

        adapterMars = MarsAdapter(
            view.context,
            plainDetail.img_list,
            plainDetail.earth_date,
            "Sol " + plainDetail.sol.toString(),
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


        viewModel.favMarsState(MarsRoom(plainDetail.earth_date, plainDetail.sol, plainDetail.maxTemp, plainDetail.minTemp), view.btnFavMars)

    }


}