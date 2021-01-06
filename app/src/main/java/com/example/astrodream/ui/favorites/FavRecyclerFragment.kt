package com.example.astrodream.ui.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astrodream.R
import com.example.astrodream.domain.Favorite
import kotlinx.android.synthetic.main.fragment_fav_recycler.view.*

class FavRecyclerFragment : Fragment(), FavAdapter.OnClickFavListener {

    private var listFavs = dummyFavData("today")
    private var adapterFav = FavAdapter(listFavs, this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_recycler, container, false)

        // Atribui o adapter criado ao adapter do RecyclerView
        view.rvFav.adapter = adapterFav

        // Cria o layout do RecyclerView
        if (container != null) {
            view.rvFav.layoutManager = LinearLayoutManager(container.context)
        }
        view.rvFav.setHasFixedSize(true)

        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir da seleção de uma tab,
            // popula o RecyclerView com os items corretos
            if (container != null) {
                // Salva dados do bundle em variaveis
                val type = requireArguments().getString("type") as String
                // Atualiza a lista de itens favoritos
                listFavs = dummyFavData(type)
                // Atualiza o adapter de acordo com a tab selecionada
                adapterFav = FavAdapter(listFavs, this)
                // Atribui o adapter criado acima ao adapter do RecyclerView
                view.rvFav.adapter = adapterFav
                // Cria o layout do RecyclerView
                view.rvFav.layoutManager = LinearLayoutManager(container.context)
                view.rvFav.setHasFixedSize(true)
            }
        } catch (e: IllegalStateException) {
            Log.e("FavRecyclerFragment", e.toString())
        }

        return view
    }

    override fun onClickFav(position: Int) {

        // Pega a posição do favorito clicado
        val fav = listFavs[position]

        // Cria um bundle com as informações do favorito
        // e navega para o respectivo fragment
        // TODO: dados de exemplo apenas, é necessário reescrever
        //  essa parte quando estivermos acessando a API pois estou
        //  apenas abrindo o mesmo "favorito" independente de qual é escolhido da lista
        when (fav.type) {
            "today" -> {
                val bundleRest: Bundle = Bundle().apply {
                    putString("title", fav.descrip1)
                    putString("date", fav.descrip2)
                    putString("img", fav.img)
                }
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_favTodayFragment,
                    bundleRest
                )
            }
            "asteroid" -> {
                val bundleRest: Bundle = Bundle().apply {
                    putString("name", "1979 XB")
                    putString("date", "17/12/1979")
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
                    putString("date", fav.descrip1)
                    putString("img", fav.img)
                }
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_favGlobeFragment,
                    bundleRest
                )
            }
            "tech" -> {
                val bundleRest: Bundle = Bundle().apply {
                    putString("typeTech", fav.descrip1)
                    putString("title", fav.descrip2)
                    putString("img", fav.img)
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
                val bundleRest: Bundle = Bundle().apply {
                    putStringArrayList(
                        "marsPicsList", arrayListOf(
                            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG",
                            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/rcam/RLB_659123404EDR_F0832382RHAZ00311M_.JPG",
                            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124657EDR_F0832382NCAM00294M_.JPG",
                            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124625EDR_F0832382NCAM00294M_.JPG",
                            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124442EDR_F0832382NCAM00294M_.JPG"
                        )
                    )
                    putString("postDate", fav.descrip2)
                    putString("maxTemp", "Máxima: -11°C")
                    putString("minTemp", "Mínima: -93°C")
                }
                findNavController().navigate(
                    R.id.action_favRecyclerFragment_to_recentMarsFragment2,
                    bundleRest
                )
            }
        }

    }

    private fun dummyFavData(type: String): ArrayList<Favorite> {
        when (type) {
            "today" -> {
                return arrayListOf(
                    Favorite(
                        type,
                        "Dark Molecular Cloud Barnard 68",
                        "22 de Novembro de 2020",
                        "https://apod.nasa.gov/apod/image/2011/barnard68v2_vlt_960.jpg"
                    ),
                    Favorite(
                        type,
                        "Mars and Meteor over Jade Dragon Snow Mountain",
                        "21 de Novembro de 2020",
                        "https://apod.nasa.gov/apod/image/2011/LeonidmeteorandMarsoverYulongsnowmountain1050.jpg"
                    ),
                    Favorite(
                        type,
                        "Global Map: Mars at Opposition",
                        "20 de Novembro de 2020",
                        "https://apod.nasa.gov/apod/image/2011/marsglobalmap_1100.jpg"
                    ),
                    Favorite(
                        type,
                        "Crew-1 Mission Launch Streak",
                        "19 de Novembro de 2020",
                        "https://apod.nasa.gov/apod/image/2011/spacex-crew-1-JenScottPhotography-11_1050.jpg"
                    ),
                    Favorite(
                        type,
                        "A Double Star Cluster in Perseus",
                        "18 de Novembro de 2020",
                        "https://apod.nasa.gov/apod/image/2011/DoubleCluster_Polanski_960.jpg"
                    )
                )
            }
            "asteroid" -> {
                return arrayListOf(
                    Favorite(
                        type,
                        "Ananda",
                        "22 de Novembro de 2020",
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Favorite(
                        type,
                        "Arthur",
                        "21 de Novembro de 2020",
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Favorite(
                        type,
                        "Marina",
                        "20 de Novembro de 2020",
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Favorite(
                        type,
                        "Rafael",
                        "19 de Novembro de 2020",
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    ),
                    Favorite(
                        type,
                        "Raul",
                        "18 de Novembro de 2020",
                        "https://media4.s-nbcnews.com/i/newscms/2017_16/1968396/170418-asteroid-mn-0730_b2c1f54812269d7ea29e5890b0c2b173.jpg"
                    )
                )
            }
            "globe" -> {
                return arrayListOf(
                    Favorite(
                        type,
                        "18 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/18/png/epic_1b_20201118001752.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        "17 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/17/png/epic_1b_20201117003633.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        "16 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/16/png/epic_1b_20201116005516.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
                        "15 de Novembro de 2020",
                        "",
                        "https://api.nasa.gov/EPIC/archive/natural/2020/11/15/png/epic_1b_20201115010437.png?api_key=k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
                    ),
                    Favorite(
                        type,
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
                        "Patente",
                        "Aircraft Engine Icing Event Avoidance and Mitigation",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LEW-TOPS-125/iStock-157730835_LEW-19309-1_airplane-storm_1388x1050-300dpi.jpg"
                    ),
                    Favorite(
                        type,
                        "Patente",
                        "Turbofan Engine Acoustic Liner Design and Analysis Tools",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LAR-TOPS-185/Front.jpg"
                    ),
                    Favorite(
                        type,
                        "Patente",
                        "Aircraft Active Pylon Noise Control System",
                        "https://technology.nasa.gov/t2media/tops/img/LAR-TOPS-179/TOP 179 front.jpg"
                    ),
                    Favorite(
                        type,
                        "Patente",
                        "Conditionally Active Min-Max Limit Regulators",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LEW-TOPS-56/iStock_000056646008_LEW-18934-1_instrument-panel_1388x1050-300DPI.jpg"
                    ),
                    Favorite(
                        type,
                        "Patente",
                        "Compact, Lightweight, CMC-Based Acoustic Liner",
                        "https://ntts-prod.s3.amazonaws.com/t2p/prod/t2media/tops/img/LEW-TOPS-61/LEW-18769-1_airplane-noise_1388X1050-300DPI.jpg"
                    )
                )
            }
            "mars" -> {
                return arrayListOf(
                    Favorite(
                        type,
                        "Sol 706",
                        "20 de Novembro de 2020",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG"
                    ),
                    Favorite(
                        type,
                        "Sol 704",
                        "18 de Novembro de 2020",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02946/opgs/edr/fcam/FLB_659019582EDR_F0831974FHAZ00337M_.JPG"
                    ),
                    Favorite(
                        type,
                        "Sol 703",
                        "17 de Novembro de 2020",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02945/opgs/edr/fcam/FLB_658949218EDR_F0831974FHAZ00337M_.JPG"
                    ),
                    Favorite(
                        type,
                        "Sol 702",
                        "16 de Novembro de 2020",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02944/opgs/edr/fcam/FLB_658838602EDR_F0831974FHAZ00341M_.JPG"
                    ),
                    Favorite(
                        type,
                        "Sol 701",
                        "15 de Novembro de 2020",
                        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02943/opgs/edr/fcam/FLB_658767605EDR_F0831974FHAZ00302M_.JPG"
                    )
                )
            }
        }
        return arrayListOf()
    }

}