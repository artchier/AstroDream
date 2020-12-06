package com.example.astrodream.ui.dailyimage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.domain.DailyImage
import com.example.astrodream.services.service
import kotlinx.android.synthetic.main.fragment_daily_history.view.*


class DailyImageHistoryFragment : Fragment(), DailyImageAdapter.OnClickDailyListener {

    companion object {
        fun newInstance() = DailyImageHistoryFragment()
    }

    private lateinit var adapterDailyHistory: DailyImageAdapter
    private lateinit var actionListener: ActionListener

    interface ActionListener {
        fun showDetailView(daily: DailyImage)
    }

    private val viewModel : DailyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_daily_history, container, false)

        // Atribui o adapter criado ao adapter do RecyclerView
        adapterDailyHistory = DailyImageAdapter(this)
        view.rvDailyHistory.adapter = adapterDailyHistory
        view.rvDailyHistory.setHasFixedSize(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.popList("today", 20)

        viewModel.listResults.observe(viewLifecycleOwner) {
            adapterDailyHistory.addList(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            actionListener = context as ActionListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ActionListener!")
        }
    }

    override fun onClickDaily(position: Int) {
        viewModel.selectDaily(position)
        actionListener.showDetailView(viewModel.listResults.value!![position])
    }
}