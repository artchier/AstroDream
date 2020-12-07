package com.example.astrodream.ui.dailyimage

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.domain.DailyImage
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.initial.InitialActivity
import com.google.android.material.tabs.TabLayout
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_daily_image.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs


class DailyImageActivity : ActivityWithTopBar(R.string.daily_image, R.id.dlDaily), DailyImageHistoryFragment.ActionListener {

    private val viewModel by viewModels<DailyViewModel> {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DailyViewModel(service) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_image)
        setUpMenuBehavior()

        AndroidThreeTen.init(this)

        addFragment(DailyImageFragment.newInstance(), "ROOT_TAG")

        viewModel.popList()

        // Nevegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val detailFrag = findFragByTAG("DETAIL_TAG")
                        if (detailFrag is DailyImageFragment) {
                            removeFragment(detailFrag)
                        }
                        viewModel.selectRoot()
                        fromHistToRoot()
                    }
                    1 -> {
                        if (supportFragmentManager.findFragmentByTag("HIST_TAG") == null) {
                            addFragment(DailyImageHistoryFragment.newInstance(), "HIST_TAG")
                        } else {
                            fromRootToHist()
                        }
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                    }
                    1 -> {
                        val detailFrag = findFragByTAG("DETAIL_TAG")
                        if (detailFrag is DailyImageFragment) {
                            supportFragmentManager.popBackStackImmediate("HIST_TAG", 0)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        // Alteração da cor dos icones dos tabs inferiores (selecionado ou não)
        val colors: ColorStateList = resources.getColorStateList(R.color.tabs_selector, theme)
        for (i in 0 until bottomTabs.tabCount) {
            val tab: TabLayout.Tab = bottomTabs.getTabAt(i)!!
            var icon = tab.icon
            if (icon != null) {
                icon = DrawableCompat.wrap(icon)
                DrawableCompat.setTintList(icon, colors)
            }
        }
    }

    override fun onBackPressed() {
        if (dlDaily.isDrawerOpen(GravityCompat.END)) {
            dlDaily.closeDrawer(GravityCompat.END)
        }

        else {
            val detailFrag = findFragByTAG("DETAIL_TAG")

            if (bottomTabs.selectedTabPosition == 0) {
                finish()
                startActivity(Intent(this, InitialActivity::class.java))
            }

            if (bottomTabs.selectedTabPosition != 0 && detailFrag !is DailyImageFragment) {
                bottomTabs.getTabAt(0)?.select()
            }

            if (bottomTabs.selectedTabPosition != 0 && detailFrag is DailyImageFragment) {
                bottomTabs.getTabAt(1)?.select()
            }
        }
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.commit {
            addToBackStack(tag)
            add(R.id.dailyContainer, fragment, tag)
        }
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            remove(fragment)
        }
    }

    private fun fromHistToRoot() {
        supportFragmentManager.commit {
            hide(supportFragmentManager.findFragmentByTag("HIST_TAG")!!)
            show(supportFragmentManager.findFragmentByTag("ROOT_TAG")!!)
        }
    }

    private fun fromRootToHist() {
        supportFragmentManager.commit {
            hide(supportFragmentManager.findFragmentByTag("ROOT_TAG")!!)
            show(supportFragmentManager.findFragmentByTag("HIST_TAG")!!)
        }
    }

    private fun findFragByTAG(tag: String) = supportFragmentManager.findFragmentByTag(tag)

    override fun showDetailView() {
        addFragment(DailyImageFragment.newInstance(), "DETAIL_TAG")
    }

}