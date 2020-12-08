package com.example.astrodream.ui.plaindailymars

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_plain_history.view.*

abstract class PlainHistoryFragment : Fragment(), PlainAdapter.OnClickDetailListener {

    lateinit var adapterHistory: PlainAdapter
    lateinit var actionListener: ActionListener

    var hasOngoingRequest = false

    interface ActionListener {
        fun showDetailView()
    }

    val viewModel : PlainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plain_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        adapterHistory = PlainAdapter(this)
        view.rvHistory.adapter = adapterHistory
        view.rvHistory.setHasFixedSize(true)

        setUpScroller(view.rvHistory, view.rvHistory.layoutManager as GridLayoutManager)

        viewModel.listResults.observe(viewLifecycleOwner) {
            adapterHistory.addList(it)
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

    override fun onClickDetail(position: Int) {
        viewModel.selectDetail(adapterHistory.listHistory[position])
        actionListener.showDetailView()
    }

    fun setUpScroller(recyclerView: RecyclerView, gridLayoutManager: GridLayoutManager) {
        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy <= 0) return

                    val lastVisbleItem = gridLayoutManager.findLastVisibleItemPosition()
                    val itens = adapterHistory.itemCount

                    if (lastVisbleItem + 6 < itens || itens > 100) return
                    if (hasOngoingRequest) return

                    hasOngoingRequest = true
                    viewModel.popList()
                }
            }
        )
    }

}