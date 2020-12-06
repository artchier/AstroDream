package com.example.astrodream.ui.dailyimage

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.initial.InitialActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_daily_image.*
import kotlinx.android.synthetic.main.activity_mars.bottomTabs


class DailyImageActivity : ActivityWithTopBar(R.string.daily_image, R.id.dlDaily) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_image)
        setUpMenuBehavior()
        addFragment(DailyImageFragment.newInstance(), "ROOT_TAG")

        // Nevegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val currFrag = currentFragment()
                        if (currFrag is DailyImageFragment) {
                            removeFragment(currFrag)
                        }
                        fromHistToRoot()
                    }
                    1 -> {
                        if (supportFragmentManager.findFragmentByTag("HIST_TAG") == null) {
                            supportFragmentManager.commit {
                                hide(supportFragmentManager.findFragmentByTag("ROOT_TAG")!!)
                            }
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
                        val currFrag = currentFragment()
                        if (currFrag is DailyImageFragment) {
                            supportFragmentManager.commit {
                                remove(currFrag)
                                show(supportFragmentManager.findFragmentByTag("HIST_TAG")!!)
                            }
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
            val currFrag = currentFragment()

            if (bottomTabs.selectedTabPosition == 0) {
                finish()
                startActivity(Intent(this, InitialActivity::class.java))
            }

            if (bottomTabs.selectedTabPosition != 0 && currFrag is DailyImageHistoryFragment) {
                bottomTabs.getTabAt(0)?.select()
            }

            if (bottomTabs.selectedTabPosition != 0 && currFrag is DailyImageFragment) {
                bottomTabs.getTabAt(1)?.select()
            }
        }
    }

    private fun addFragment(fragment: Fragment, tag: String) {
//        val tag = if (fragment is DailyImageHistoryFragment) "FRAG_ADDED_HIST_TAG" else "FRAG_ADDED_DAILY_TAG"
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

    private fun currentFragment(): Fragment = supportFragmentManager.findFragmentById(R.id.dailyContainer) as Fragment

}