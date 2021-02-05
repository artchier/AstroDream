package com.example.astrodream.ui.mars

import androidx.fragment.app.Fragment
import com.example.astrodream.R
import com.example.astrodream.ui.plaindailymars.PlainActivity
import com.example.astrodream.ui.plaindailymars.PlainActivityType
import kotlinx.android.synthetic.main.activity_globe.*

class MarsActivity : PlainActivity(R.string.marte, PlainActivityType.Mars) {

    override fun newDetailFrag(): Fragment {
        return RecentMarsFragment.newInstance()
    }

    override fun newHistoryFrag(): Fragment {
        return HistoryMarsFragment.newInstance()
    }

    override fun onStop() {
        super.onStop()

        realtimeViewModel.updateUserNasaCoins(
            realtimeViewModel.activeUser.value?.email!!,
            RecentMarsFragment.newNasaCoinsValue
        )
    }
}