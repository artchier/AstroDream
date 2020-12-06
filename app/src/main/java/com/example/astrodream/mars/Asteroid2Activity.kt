package com.example.astrodream.mars

import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.astrodream.ActivityWithTopBar
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroids
import com.example.astrodream.domain.AsteroidsAdapter
import com.example.astrodream.domain.AsteroidsAdapter.OnClickAsteroidsListener
import com.example.astrodream.domain.ExpandableListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sysdata.widget.accordion.ItemAdapter.OnItemClickedListener
import kotlinx.android.synthetic.main.activity_asteroid2.*


class Asteroid2Activity : ActivityWithTopBar(R.string.asteroides, R.id.dlAsteroids2) {
    private lateinit var listView: ExpandableListView
    private val expandableListAdapter = ExpandableListAdapter(this)
    private val listButtonsName = ArrayList<String>()
    private val listAsteroids = HashMap<String, ArrayList<Asteroids>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid2)

        listView = exp_list_view_asteroids
        expandableListAdapter.addListButtons(listButtonsName)
        expandableListAdapter.addListAsteroids(listAsteroids)

        listView.setOnChildClickListener{ parent, view, groupPosition, childPosition, id ->

            false
        }

        getListas()

        listView.setAdapter(expandableListAdapter)

        setUpMenuBehavior()
    }

    private fun getListas() {

    listButtonsName.addAll(arrayListOf("Listar asteroides próximos", "Listar asteroides por nome", "Listar asteroides por data", "Listar asteroides perigosos"))
    val list = arrayListOf(
            Asteroids("Ananda", "24/11/2020"),
            Asteroids("Arthur", "24/11/2020"),
            Asteroids("Marina", "24/11/2020"),
            Asteroids("Rafael", "24/11/2020"),
            Asteroids("Raul", "24/11/2020")
        )
        listAsteroids[listButtonsName[0]] = list
        listAsteroids[listButtonsName[1]] = list
        listAsteroids[listButtonsName[2]] = list
        listAsteroids[listButtonsName[3]] = list
    }

    fun onClickAsteroids(viewHolder: AsteroidsAdapter.AsteroidsViewHolder, position: Int) {
        run {
            MaterialAlertDialogBuilder(this)
                .setBackgroundInsetStart(70)
                .setBackgroundInsetEnd(70)
                .setBackgroundInsetTop(10)
                .setBackgroundInsetBottom(100)
                .setBackground(
                    ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
                )
                .setView(R.layout.astrodialog)
                .show()
        }
    }

}