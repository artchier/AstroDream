package com.example.astrodream

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*
import me.relex.circleindicator.CircleIndicator

class RecentMarsFragment : Fragment() {

    lateinit var marsPicsList: ArrayList<String>
    lateinit var adapterMars: MarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_recent_mars, container, false)

        // Lista de imagens "mais recentes"
        marsPicsList = getMarsPics()

        // Cria adapter com a lista de imagens
        if (container != null) {
            adapterMars = MarsAdapter(container.getContext(), marsPicsList, "20 de Novembro", "Máxima: -11°C", "Mínima: -93°C")
        }

        // Atribui o adapter criado acima ao adapter do ViewPager
        view.vpMarsRecent.adapter = adapterMars

        // Inclui o indicador de bolinhas
        val indicator = view.ciMarsRecent as CircleIndicator
        indicator.setViewPager(view.vpMarsRecent)

        try {
            // Caso tenha dados no bundle, ou seja, o fragment foi carregado a partir do Historico,
            // atualiza as imagens e textos de acordo com a informação do post clicado lá no Historico
            if (requireArguments() != null) {
                if (container != null) {
                    // Salva dados do bundle em variaveis
                    val listFromBundle: ArrayList<String> = requireArguments().getStringArrayList("marsPicsList") as ArrayList<String>
                    val postDate = requireArguments().getString("postDate") as String
                    val maxTemp = requireArguments().getString("maxTemp") as String
                    val minTemp = requireArguments().getString("minTemp") as String
                    // Acerta o texto acima da imagem para mostrar o dia do post
                    view.postDescr.text = "Post do dia $postDate"
                    // Cria o adapter com a informação do post clicado lá no Historico
                    adapterMars = MarsAdapter(container.getContext(), listFromBundle, postDate, maxTemp, minTemp)
                    // Atribui o adapter criado acima ao adapter do ViewPager
                    view.vpMarsRecent.adapter = adapterMars
                    // Inclui o indicador de bolinhas
                    val indicator = view.ciMarsRecent as CircleIndicator
                    indicator.setViewPager(view.vpMarsRecent)
                }
            }
        } catch (e: Exception) {
            Log.e("RecentMarsFragment", e.toString())
        }

        return view
    }


    // Imagens do dia 20/11/2020
    fun getMarsPics() = arrayListOf(
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/fcam/FLB_659123269EDR_F0832382FHAZ00302M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/rcam/RLB_659123404EDR_F0832382RHAZ00311M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124657EDR_F0832382NCAM00294M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124625EDR_F0832382NCAM00294M_.JPG",
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/02947/opgs/edr/ncam/NLB_659124442EDR_F0832382NCAM00294M_.JPG"
    )

}