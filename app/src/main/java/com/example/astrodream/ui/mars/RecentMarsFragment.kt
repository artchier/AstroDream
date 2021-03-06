package com.example.astrodream.ui.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ToggleButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.ui.plaindailymars.PlainDetailFragment
import com.example.astrodream.ui.plaindailymars.PlainViewModel
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*
import me.relex.circleindicator.CircleIndicator
import com.example.astrodream.services.ServiceDBImplementationMars
import com.example.astrodream.services.service
import com.example.astrodream.ui.RealtimeViewModel
import com.example.astrodream.ui.plaindailymars.PlainActivity
import com.example.astrodream.ui.plaindailymars.PlainActivityType
import kotlinx.android.synthetic.main.card_mars_post.*
import kotlinx.android.synthetic.main.card_mars_post.view.*
import kotlinx.android.synthetic.main.fragment_recent_mars.*

class RecentMarsFragment : PlainDetailFragment(R.layout.fragment_recent_mars) {
    companion object {
        fun newInstance() = RecentMarsFragment()
    }

    lateinit var adapterMars: MarsAdapter
    val viewModel: PlainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextActivity = this.requireActivity()
        view.btnFavMars.setOnClickListener {
            if (contextActivity is PlainActivity) {
                val viewModel: PlainViewModel by activityViewModels()
                viewModel.favPlainDB(plainDetail, it as ToggleButton, requireActivity())
            } else {
                val db = AppDatabase.invoke(contextActivity)
                val repositoryDaily = ServiceDBImplementationMars(db.marsDAO())
                val viewModel by viewModels<PlainViewModel> {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                            return PlainViewModel(
                                service,
                                PlainActivityType.Mars,
                                repositoryDaily
                            ) as T
                        }
                    }
                }
                viewModel.favPlainDB(plainDetail, it as ToggleButton, requireActivity())
            }
        }
    }

    override fun popView(view: View) {
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

        view.btnFavMars.isChecked = plainDetail.isFav
    }

}