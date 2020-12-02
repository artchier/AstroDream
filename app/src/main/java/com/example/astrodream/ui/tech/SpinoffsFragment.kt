package com.example.astrodream.ui.tech

import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astrodream.R
import com.example.astrodream.domain.Spinoff
import kotlinx.android.synthetic.main.fragment_spinoffs.view.*
import kotlinx.android.synthetic.main.item_spinoff.*

class SpinoffsFragment : Fragment(), SpinoffsAdapter.OnClickSpinoffListener {
    private lateinit var contextTechActivity : TechActivity
    val listSpinoffs = getAllSpinoffs()
    val adapterSpinoffs = SpinoffsAdapter(listSpinoffs, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_spinoffs, container, false)

        view.rvSpinoffs.adapter = adapterSpinoffs
        view.rvSpinoffs.layoutManager = LinearLayoutManager(contextTechActivity)
        view.rvSpinoffs.setHasFixedSize(true)

        return view
    }

    fun getAllSpinoffs(): ArrayList<Spinoff> {
        val Spinoff = Spinoff(1,
            R.drawable.ic_tecnologia, "GRC-SO-147", "Revestimentos de motores", "O aumento da temperatura operacional dos motores a turbina reduz o consumo de combustível e aumenta a eficiência do motor. No entanto, os componentes do motor devem ser protegidos do calor excessivo. O Lewis Research Center desenvolveu com sucesso revestimentos de barreira térmica (TBCs), que são depositados nos componentes. Eles isolam, oferecem resistência à oxidação e corrosão e aumentam a aderência. As temperaturas da superfície podem ser reduzidas em 200 graus centígrados ou mais. G. E. Aircraft Engines, um empreiteiro Lewis, agora usa um TBC baseado no desenvolvido em Lewis, em motores de produção. O sistema, que consiste em um adesivo e uma camada de acabamento, estende a vida útil do componente de 1,3 para 2 vezes. A empresa também está testando TBCs em componentes que operam em temperaturas mais altas.")

        return arrayListOf(Spinoff)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TechActivity) contextTechActivity = context
    }

    override fun onClickSpinoff(position: Int) {
        if (llDescSpinoff.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(cvSpinoff, AutoTransition())
            llDescSpinoff.visibility = View.VISIBLE
        } else {
            TransitionManager.beginDelayedTransition(cvSpinoff, AutoTransition())
            llDescSpinoff.visibility = View.GONE
        }
    }
}