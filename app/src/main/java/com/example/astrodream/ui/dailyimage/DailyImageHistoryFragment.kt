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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var hasOngoingRequest = false

    interface ActionListener {
        fun showDetailView()
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

        setUpScroller(view.rvDailyHistory, view.rvDailyHistory.layoutManager as GridLayoutManager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listResults.observe(viewLifecycleOwner) {
            adapterDailyHistory.addList(it)
            hasOngoingRequest = false
        }
        hasOngoingRequest = true
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
        viewModel.selectDaily(adapterDailyHistory.listDailyPics[position])
        actionListener.showDetailView()
    }

    private fun setUpScroller(recyclerView: RecyclerView, gridLayoutManager: GridLayoutManager) {
        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy <= 0) return

                    val lastVisbleItem = gridLayoutManager.findLastVisibleItemPosition()
                    val itens = adapterDailyHistory.itemCount

                    if (lastVisbleItem + 6 < itens || itens > 100) return
                    if (hasOngoingRequest) return

                    hasOngoingRequest = true
                    viewModel.popList()
                }
            }
        )
    }
}