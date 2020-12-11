package com.example.astrodream.ui.mars

import androidx.fragment.app.Fragment
import com.example.astrodream.R
import com.example.astrodream.ui.plaindailymars.PlainActivity

class MarsActivity : PlainActivity(R.string.marte, "Mars") {

    override fun newDetailFrag(): Fragment {
        return RecentMarsFragment.newInstance()
    }

    override fun newHistoryFrag(): Fragment {
        return HistoryMarsFragment.newInstance()
    }
}