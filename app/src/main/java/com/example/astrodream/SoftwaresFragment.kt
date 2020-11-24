package com.example.astrodream

import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_softwares.view.*
import kotlinx.android.synthetic.main.item_software.*

class SoftwaresFragment : Fragment(), SoftwaresAdapter.OnClickSoftwareListener {

    private lateinit var contextTechActivity : TechActivity
    private val listSoftwares = getAllSoftware()
    private val adapterSoftwares = SoftwaresAdapter(listSoftwares, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_softwares, container, false)

        view.rvSoftwares.adapter = adapterSoftwares
        view.rvSoftwares.layoutManager = LinearLayoutManager(contextTechActivity)
        view.rvSoftwares.setHasFixedSize(true)

        return view
    }

    private fun getAllSoftware(): ArrayList<Software> {
        val software = Software(1, R.drawable.ic_tecnologia, "LAR-19151-1", "Motor de anomalia hidrológica", "Os inovadores do Glenn Research Center da NASA desenvolveram um novo meio de evitar e mitigar eventos de congelamento em aeronaves voando acima de 14.000 pés, melhorando drasticamente a segurança da aviação e reduzindo os custos operacionais. Freqüentemente indetectáveis \u200B\u200Bcom o radar atual, os cristais de gelo em células de tempestade convectivas podem produzir um fenômeno conhecido como Ice Crystal Icing, no qual o gelo se acumula, ou se acumula, em motores turbofan. O acúmulo de cristais de gelo pode causar sérios problemas operacionais e, às vezes, até mesmo falhas catastróficas do motor. Usando uma combinação de sensores, modelagem do sistema do motor e código de análise de fluxo do compressor, a inovação de Glenn realiza análises em tempo real para determinar o potencial de acúmulo de gelo. Esta análise permite que os pilotos evitem a formação de gelo potencial enquanto usam uma rota mais direta do que seria possível. Assim, o sistema de Glenn reduz o consumo de combustível e o desgaste do motor, cumprindo o objetivo crucial de aumentar a segurança da aeronave.")

        return arrayListOf(software)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }

    override fun onClickSoftware(position: Int) {
        if (llDescSoftware.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(cvSoftware, AutoTransition())
            llDescSoftware.visibility = View.VISIBLE
        } else {
            TransitionManager.beginDelayedTransition(cvSoftware, AutoTransition())
            llDescSoftware.visibility = View.GONE
        }
    }
}