package com.example.astrodream.ui.plaindailymars

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.showErrorInternetConnection
import com.example.astrodream.services.*
import com.example.astrodream.ui.ActivityWithTopBar
import com.example.astrodream.ui.initial.InitialActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_mars.bottomTabs
import kotlinx.android.synthetic.main.activity_plain.*
import kotlinx.android.synthetic.main.activity_tech.*
import kotlinx.android.synthetic.main.dialog_info_daily.view.*

abstract class PlainActivity(toolbarTitleString: Int, val type: PlainActivityType) :
    ActivityWithTopBar(toolbarTitleString, R.id.dlPlain), PlainHistoryFragment.ActionListener {

    // TODO: tentar implementar o Navigation conforme o FavoritesActivity
    /*
    * Se esse construtor não estiver aqui,
    * a linha <activity android:name=".ui.plaindailymars.PlainActivity" />
    * no AndroidManifest.xml fica vermelha e pede um construtor vazio e sem parâmetros
    */
    constructor() : this(0, PlainActivityType.DailyImage)

    private lateinit var db: AppDatabase
    private lateinit var repositoryDaily: ServiceDBDaily
    private lateinit var repositoryMars: ServiceDBMars

    val viewModel by viewModels<PlainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (type == PlainActivityType.DailyImage) {
                    return PlainViewModel(service, type, repositoryDaily) as T
                } else {
                    return PlainViewModel(service, type, repositoryMars) as T
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plain)
        setUpMenuBehavior()

        db = AppDatabase.invoke(this)
        if (type == PlainActivityType.DailyImage) {
            repositoryDaily = ServiceDBImplementationDaily(db.dailyDAO())
        } else if (type == PlainActivityType.Mars) {
            repositoryMars = ServiceDBImplementationMars(db.marsDAO())
        }

        AndroidThreeTen.init(this)

        addFragment(newDetailFrag(), "ROOT_TAG")

        piImage.show()
        viewModel.populateList()

        // Navegação entre os tabs inferiores
        bottomTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        fromHistToRoot()
                    }
                    1 -> {
                        fromRootToHist()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    1 -> {
                        reselectHist()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        // Configurações da tab inferior
        configBottomTabs()

        // Dialog em caso de perda de conexão
        viewModel.hasInternetConnection.observe(this) {
            if (!it) {
                AstroDreamUtil.showErrorInternetConnection(this)
            }
        }

        realtimeViewModel.activeUser.observe(this) {
            tvTotalMars.text = it.nasaCoins.toString()
        }
    }

    override fun onBackPressed() {
        if (dlPlain.isDrawerOpen(GravityCompat.END)) {
            dlPlain.closeDrawer(GravityCompat.END)
            return
        }

        val detailFrag = findFragByTAG("DETAIL_TAG")

        if (bottomTabs.selectedTabPosition == 0) {
            finish()
            startActivity(Intent(this, InitialActivity::class.java))
            return
        }

        if (detailFrag !is PlainDetailFragment) {
            bottomTabs.getTabAt(0)?.select()
            return
        }

        bottomTabs.getTabAt(1)?.select()
    }

    abstract fun newDetailFrag(): Fragment
    abstract fun newHistoryFrag(): Fragment

    private fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.commit {
            addToBackStack(tag)
            add(R.id.plainContainer, fragment, tag)
        }
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            remove(fragment)
        }
    }

    fun fromHistToRoot() {
        val detailFrag = findFragByTAG("DETAIL_TAG")
        if (detailFrag is PlainDetailFragment) {
            removeFragment(detailFrag)
        }
        viewModel.selectRoot()
        supportFragmentManager.commit {
            hide(supportFragmentManager.findFragmentByTag("HIST_TAG")!!)
            show(supportFragmentManager.findFragmentByTag("ROOT_TAG")!!)
        }
    }

    fun fromRootToHist() {
        if (supportFragmentManager.findFragmentByTag("HIST_TAG") == null) {
            addFragment(newHistoryFrag(), "HIST_TAG")
        } else {
            supportFragmentManager.commit {
                hide(supportFragmentManager.findFragmentByTag("ROOT_TAG")!!)
                show(supportFragmentManager.findFragmentByTag("HIST_TAG")!!)
            }
        }
    }

    private fun reselectHist() {
        val detailFrag = findFragByTAG("DETAIL_TAG")
        if (detailFrag is PlainDetailFragment) {
            supportFragmentManager.popBackStackImmediate("HIST_TAG", 0)
        }
    }

    private fun findFragByTAG(tag: String) = supportFragmentManager.findFragmentByTag(tag)

    override fun showDetailView() {
        addFragment(newDetailFrag(), "DETAIL_TAG")
    }

    private fun configBottomTabs() {
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

    override fun onStop() {
        super.onStop()

        realtimeViewModel.updateUserNasaCoins(
            realtimeViewModel.activeUser.value?.email!!,
            tvTotalMars.text.toString().toLong()
        )
    }
}