package com.example.astrodream.ui.plaindailymars

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.initial.InitialActivity
import com.google.android.material.tabs.TabLayout
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_mars.bottomTabs
import kotlinx.android.synthetic.main.activity_plain.*

abstract class PlainActivity(toolbarTitleString: Int, val type: PlainActivityType)
    : ActivityWithTopBar(toolbarTitleString, R.id.dlPlain), PlainHistoryFragment.ActionListener {

    /*
    * Se esse construtor não estiver aqui,
    * a linha <activity android:name=".ui.plaindailymars.PlainActivity" />
    * no AndroidManifest.xml fica vermelha e pede um construtor vazio e sem parâmetros
    */
    constructor() : this(0, PlainActivityType.DailyImage)

    val viewModel by viewModels<PlainViewModel> {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PlainViewModel(service, type) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plain)
        setUpMenuBehavior()

        AndroidThreeTen.init(this)

        addFragment(newDetailFrag(), "ROOT_TAG")

        piImage.show()
        viewModel.populateList()

        // Navegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            val detailFrag = findFragByTAG("DETAIL_TAG")
                            if (detailFrag is PlainDetailFragment) {
                                removeFragment(detailFrag)
                            }
                            viewModel.selectRoot()
                            fromHistToRoot()
                        }
                        1 -> {
                            if (supportFragmentManager.findFragmentByTag("HIST_TAG") == null) {
                                addFragment(newHistoryFrag(), "HIST_TAG")
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
                            if (detailFrag is PlainDetailFragment) {
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
        if (dlPlain.isDrawerOpen(GravityCompat.END)) {
            dlPlain.closeDrawer(GravityCompat.END)
        }

        else {
            val detailFrag = findFragByTAG("DETAIL_TAG")

            if (bottomTabs.selectedTabPosition == 0) {
                finish()
                startActivity(Intent(this, InitialActivity::class.java))
            }

            if (bottomTabs.selectedTabPosition != 0 && detailFrag !is PlainDetailFragment) {
                bottomTabs.getTabAt(0)?.select()
            }

            if (bottomTabs.selectedTabPosition != 0 && detailFrag is PlainDetailFragment) {
                bottomTabs.getTabAt(1)?.select()
            }
        }
    }

    abstract fun newDetailFrag(): Fragment
    abstract fun newHistoryFrag(): Fragment

    fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.commit {
            addToBackStack(tag)
            add(R.id.plainContainer, fragment, tag)
        }
    }

    fun removeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            remove(fragment)
        }
    }

    fun fromHistToRoot() {
        supportFragmentManager.commit {
            hide(supportFragmentManager.findFragmentByTag("HIST_TAG")!!)
            show(supportFragmentManager.findFragmentByTag("ROOT_TAG")!!)
        }
    }

    fun fromRootToHist() {
        supportFragmentManager.commit {
            hide(supportFragmentManager.findFragmentByTag("ROOT_TAG")!!)
            show(supportFragmentManager.findFragmentByTag("HIST_TAG")!!)
        }
    }

    fun findFragByTAG(tag: String) = supportFragmentManager.findFragmentByTag(tag)

    override fun showDetailView() {
        addFragment(newDetailFrag(), "DETAIL_TAG")
    }

}