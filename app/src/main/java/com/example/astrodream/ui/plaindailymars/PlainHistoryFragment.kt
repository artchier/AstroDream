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
import kotlinx.android.synthetic.main.fragment_plain_history.*
import kotlinx.android.synthetic.main.fragment_plain_history.view.*

abstract class PlainHistoryFragment : Fragment(), PlainAdapter.OnClickDetailListener {

    private lateinit var actionListener: ActionListener

    val maxItems = 96

    val viewModel : PlainViewModel by activityViewModels()

    interface ActionListener {
        fun showDetailView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plain_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.adapterHistory.listener = this
        view.rvHistory.adapter = viewModel.adapterHistory

        setUpScroller(view.rvHistory, view.rvHistory.layoutManager as GridLayoutManager)

        viewModel.listResults.observe(viewLifecycleOwner) {
            // This observer is triggered when this fragment view is created
            // The if condition avoids adding the 'lisResults' at this event and considers only
            // real state changes
            if (viewModel.numFetches > 0 && viewModel.hasOngoingRequest.value!!) {
                view.rvHistory.suppressLayout(true)
                viewModel.adapterHistory.addList(it)
                view.rvHistory.suppressLayout(false)
            }
        }
        viewModel.hasOngoingRequest.observe(viewLifecycleOwner) {
            if (!it) {
                // If last item of RecyclerView is visible, request new fetch
                if ((view.rvHistory.layoutManager as GridLayoutManager).findLastVisibleItemPosition() == viewModel.adapterHistory.itemCount - 1 && viewModel.adapterHistory.itemCount < maxItems) {
                    viewModel.populateList()
                }
                // Else, hide progress indicator (spinner)
                else { piRecycler.visibility = View.INVISIBLE }
            }
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

    override fun onClickDetail(position: Int) {
        if (viewModel.adapterHistory.listHistory[position].earth_date != "" || viewModel.adapterHistory.listHistory[position].date != "") {
            viewModel.selectDetail(viewModel.adapterHistory.listHistory[position])
            actionListener.showDetailView()
        }
    }

    private fun setUpScroller(recyclerView: RecyclerView, gridLayoutManager: GridLayoutManager) {
        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()
                    val items = viewModel.adapterHistory.itemCount

                    if (dy < 0) return

                    if (lastVisibleItem + 6 < items || items >= maxItems) return
                    if (viewModel.hasOngoingRequest.value!!) return

//                    hasOngoingRequest = true
                    piRecycler.visibility = View.VISIBLE
                    viewModel.populateList()
                }
            }
        )
    }

}