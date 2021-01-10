package com.example.astrodream.ui.favorites

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astrodream.R
import com.example.astrodream.domain.*
import kotlinx.android.synthetic.main.fragment_fav_recycler.*
import kotlinx.android.synthetic.main.fragment_fav_recycler.view.*


class FavRecyclerFragment : Fragment(), FavAdapter.OnClickFavListener {

    private lateinit var listFavs: List<Any>
    private lateinit var adapterFav: FavAdapter
    private lateinit var recyclerViewState: Parcelable // variavel para salvar a posição do Recycler

    val viewModel: FavViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_recycler, container, false)

        // Retoma a posição do Recycler, caso exista um estado anterior salvo
        if (::recyclerViewState.isInitialized) { // O estado do Recycler está sendo inicializado no onDestroyView
            view.rvFav.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }

        if (container != null) {
            val type = viewModel.favType.value!!
            // Atualiza a lista de itens favoritos
            listFavs = dummyFavData(type) // TODO: passar dados do db ao invés dessa lista padrão
            // Atualiza o adapter de acordo com a tab selecionada
            adapterFav = FavAdapter(listFavs, this, type)
            // Atribui o adapter criado acima ao adapter do RecyclerView
            view.rvFav.adapter = adapterFav
            // Cria o layout do RecyclerView
            view.rvFav.layoutManager = LinearLayoutManager(container.context)
            view.rvFav.setHasFixedSize(true)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerViewState = rvFav.layoutManager?.onSaveInstanceState()!!
    }

    override fun onClickFav(position: Int) {

        // Pega a posição do favorito clicado
        val fav = listFavs[position]

        // Passa dados para o viewModel
        // e navega para o respectivo fragment
        // TODO: asteroide, globo e tech => passar dados para o viewModel e buscar fragment correto
        when (viewModel.favType.value) {
            "daily" -> {
                viewModel.selectDetail(listFavs[position] as PlainClass)
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_dailyImageFragment
                )
            }
            "asteroid" -> {
                val bundleRest: Bundle = Bundle().apply {
                    putString("name", (fav as Asteroid).name)
                    putString("date", fav.close_approach_data)
                    putString(
                        "img",
                        "https://s.yimg.com/ny/api/res/1.2/3P2Yc9yGrc99m.i3sSXWwA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTk2MA--/https://media.zenfs.com/pt-br/canal_tech_990/800089564cde284dc55f155406c8e54e"
                    )
                }
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_asteroidOrbitFragment,
                    bundleRest
                )
            }
            "globe" -> {
                val bundleRest: Bundle = Bundle().apply {
                    putString("date", (fav as Favorite).data1)
                    putString("img", fav.data3)
                }
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_favGlobeFragment,
                    bundleRest
                )
            }
            "tech" -> {
                val bundleRest: Bundle = Bundle().apply {
                    putString("typeTech", (fav as Favorite).data1)
                    putString("title", fav.data2)
                    putString("img", fav.data3)
                    putString(
                        "details",
                        "Os inovadores do Glenn Research Center da NASA desenvolveram um novo meio de evitar e mitigar eventos de congelamento em aeronaves voando acima de 14.000 pés, melhorando drasticamente a segurança da aviação e reduzindo os custos operacionais. Freqüentemente indetectáveis \u200B\u200Bcom o radar atual, os cristais de gelo em células de tempestade convectivas podem produzir um fenômeno conhecido como Ice Crystal Icing, no qual o gelo se acumula, ou se acumula, em motores turbofan. O acúmulo de cristais de gelo pode causar sérios problemas operacionais e, às vezes, até mesmo falhas catastróficas do motor. Usando uma combinação de sensores, modelagem do sistema do motor e código de análise de fluxo do compressor, a inovação de Glenn realiza análises em tempo real para determinar o potencial de acúmulo de gelo. Esta análise permite que os pilotos evitem a formação de gelo potencial enquanto usam uma rota mais direta do que seria possível. Assim, o sistema de Glenn reduz o consumo de combustível e o desgaste do motor, cumprindo o objetivo crucial de aumentar a segurança da aeronave."
                    )
                }
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_favTechFragment,
                    bundleRest
                )
            }
            "mars" -> {
                viewModel.selectDetail(listFavs[position] as PlainClass)
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_recentMarsFragment2
                )
            }
        }
    }

    // TODO: dados de exemplo apenas, é necessário buscar dados do db
    // TODO: globo e tech -> usar classe Favorite?
    private fun dummyFavData(type: String): List<Any> {
        when (type) {
            "daily" -> {
                return listOf(
                    PlainClass(
                        title = "Dark Molecular Cloud Barnard 68",
                        date = "22/11/2020",
                        url = "https://apod.nasa.gov/apod/image/2011/barnard68v2_vlt_960.jpg",
                        explanation = "xxxxxx"
                    ),
                    PlainClass(
                        title = "Mars and Meteor over Jade Dragon Snow Mountain",
                        date = "21/11/2020",
                        url = "https://apod.nasa.gov/apod/image/2011/LeonidmeteorandMarsoverYulongsnowmountain1050.jpg",
                        explanation = "yyyyyy"
                    ),
                    PlainClass(
                        title = "Global Map: Mars at Opposition",
                        date = "20/11/2020",
                        url = "https://apod.nasa.gov/apod/image/2011/marsglobalmap_1100.jpg",
                        explanation = "zzzzzz"
                    ),
                    PlainClass(
                        title = "Crew-1 Mission Launch Streak",
                        date = "19/11/2020",
                        url = "https://apod.nasa.gov/apod/image/2011/spacex-crew-1-JenScottPhotography-11_1050.jpg",
                        explanation = "aaaaa"
                    ),
                    PlainClass(
                        title = "A Double Star Cluster in Perseus",
                        date = "18/11/2020",
                        url = "https://apod.nasa.gov/apod/image/2011/DoubleCluster_Polanski_960.jpg",
                        explanation = "bbbbb"
                    )
                )
            }
            "asteroid" -> {
                return listOf(
                    Asteroid(
                        "1",
                        "Ananda",
                        true,
                        "100",
                        null,
                        "22/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Arthur",
                        true,
                        "100",
                        null,
                        "21/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Marina",
                        true,
                        "100",
                        null,
                        "20/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Rafael",
                        true,
                        "100",
                        null,
                        "19/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Asteroid(
                        "1",
                        "Raul",
                        true,
                        "100",
                        null,
                        "18/11/2020",
                        null,
                        "150",
                        null,
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    )
                )
            }
            "globe" -> {
                return arrayListOf(
                    Favorite(
                        type,
                        1,
                        "18 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/18/png/epic_1b_20201118001752.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        2,
                        "17 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/17/png/epic_1b_20201117003633.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        3,
                        "16 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/16/png/epic_1b_20201116005516.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        4,
                        "15 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/15/png/epic_1b_20201115010437.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        5,
                        "07 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/07/png/epic_1b_20201107023357.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    )
                )
            }
            "tech" -> {
                return arrayListOf(
                    Favorite(
                        type,
                        1,
                        "Patente",
                        "Aircraft Engine Icing Event Avoidance and Mitigation",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LEW-TOPS-125/iStock-157730835_LEW-19309-1_airplane-storm_1388x1050-300dpi.jpg"
                    ),
                    Favorite(
                        type,
                        2,
                        "Patente",
                        "Turbofan Engine Acoustic Liner Design and Analysis Tools",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LAR-TOPS-185/Front.jpg"
                    ),
                    Favorite(
                        type,
                        3,
                        "Patente",
                        "Aircraft Active Pylon Noise Control System",
                        "https://technology.nasa.gov/t2media/tops/img/LAR-TOPS-179/TOP 179 front.jpg"
                    ),
                    Favorite(
                        type,
                        4,
                        "Patente",
                        "Conditionally Active Min-Max Limit Regulators",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LEW-TOPS-56/iStock_000056646008_LEW-18934-1_instrument-panel_1388x1050-300DPI.jpg"
                    ),
                    Favorite(
                        type,
                        5,
                        "Patente",
                        "Compact, Lightweight, CMC-Based Acoustic Liner",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LEW-TOPS-61/LEW-18769-1_airplane-noise_1388X1050-300DPI.jpg"
                    )
                )
            }
            "mars" -> {
                return listOf(
                    PlainClass(
                        earth_date = "20/11/2020",
                        sol = 706,
                        img_list = listOf(MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG")),
                    ),
                    PlainClass(
                        earth_date = "19/11/2020",
                        sol = 705,
                        img_list = listOf(MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG")),
                    ),
                    PlainClass(
                        earth_date = "18/11/2020",
                        sol = 704,
                        img_list = listOf(MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG")),
                    ),
                    PlainClass(
                        earth_date = "17/11/2020",
                        sol = 703,
                        img_list = listOf(MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG")),
                    ),
                    PlainClass(
                        earth_date = "16/11/2020",
                        sol = 702,
                        img_list = listOf(MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG"),
                            MarsImage(1, Camera("", ""), "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG")),
                    )
                )
            }
        }
        return arrayListOf()
    }

}