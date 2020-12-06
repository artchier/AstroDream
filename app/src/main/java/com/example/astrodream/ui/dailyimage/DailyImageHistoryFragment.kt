package com.example.astrodream.ui.dailyimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.astrodream.R
import com.example.astrodream.domain.DailyImage
import kotlinx.android.synthetic.main.fragment_daily_history.view.*


class DailyImageHistoryFragment : Fragment(), DailyImageAdapter.OnClickDailyListener {

    var listDailyPics = getDailyPics()

    companion object {
        fun newInstance() = DailyImageHistoryFragment()
    }

    private val adapterDailyHistory = DailyImageAdapter(listDailyPics, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_daily_history, container, false)

        // Atribui o adapter criado ao adapter do RecyclerView
        view.rvDailyHistory.adapter = adapterDailyHistory

        // Cria o layout do RecyclerView com 3 itens por linha
        if (container != null) {
            view.rvDailyHistory.layoutManager = GridLayoutManager(container.context, 3)
        }
        view.rvDailyHistory.setHasFixedSize(true)

        return view
    }

    // Ao clicar em um post de determinado dia:
    override fun onClickDaily(position: Int) {
        // Pega o post clicado
        val daily = listDailyPics[position]
        // Cria um bundle com as informações do post para enviar para o fragment
        // onde as imagens desse dia serão carregadas (RecentMarsFragment)
        val bundleRest: Bundle = Bundle().apply {
            putString("img", daily.img)
            putString("date", daily.date)
            putString("title", daily.title)
        }
        // Navega para o RecentMarsFragment com o bundle
        val fragment: Fragment = DailyImageFragment.newInstance()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundleRest
        fragmentTransaction.add(R.id.dailyContainer, fragment)
        //fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

        private fun getDailyPics() : ArrayList<DailyImage> {

            val day1 = DailyImage(
                "Dark Molecular Cloud Barnard 68",
                "22 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/barnard68v2_vlt_960.jpg"
            )
            val day2 = DailyImage(
                "Mars and Meteor over Jade Dragon Snow Mountain",
                "21 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/LeonidmeteorandMarsoverYulongsnowmountain1050.jpg"
            )
            val day3 = DailyImage(
                "Global Map: Mars at Opposition",
                "20 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/marsglobalmap_1100.jpg"
            )
            val day4 = DailyImage(
                "Crew-1 Mission Launch Streak",
                "19 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/spacex-crew-1-JenScottPhotography-11_1050.jpg"
            )
            val day5 = DailyImage(
                "A Double Star Cluster in Perseus",
                "18 de Novembro de 2020",
                "https://apod.nasa.gov/apod/image/2011/DoubleCluster_Polanski_960.jpg"
            )
            return arrayListOf(
                day1,
                day2,
                day3,
                day4,
                day5,
                day1,
                day2,
                day3,
                day4,
                day5,
                day1,
                day2,
                day3,
                day4,
                day5
            )
        }

}