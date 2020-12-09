package com.example.astrodream.mars

import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupCollapseListener
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.astrodream.ActivityWithTopBar
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroids
import com.example.astrodream.domain.ExpandableListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_asteroid2.*


class Asteroid2Activity : ActivityWithTopBar(R.string.asteroides, R.id.dlAsteroids2) {
    private lateinit var listView: ExpandableListView
    private val expandableListAdapter = ExpandableListAdapter(this)
    private val listButtonsName = ArrayList<String>()
    private val listAsteroids = HashMap<String, ArrayList<Asteroids>>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid2)

        getListas()
        navController = findNavController(R.id.fl_imagem_asteroids)

        listView = exp_list_view_asteroids
        expandableListAdapter.addListButtons(listButtonsName)
        expandableListAdapter.addListAsteroids(listAsteroids)

        listView.setOnChildClickListener{ parent, view, groupPosition, childPosition, id ->
                onClickAsteroids(childPosition)
            false
        }

        listView.setOnGroupExpandListener(OnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                listButtonsName[groupPosition] + " Expanded",
                Toast.LENGTH_SHORT
            ).show()
        })

        listView.setOnGroupCollapseListener(OnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                listButtonsName[groupPosition] + " Collapsed",
                Toast.LENGTH_SHORT
            ).show()
        })

        listView.setAdapter(expandableListAdapter)



        //setUpMenuBehavior()
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

    private fun onClickAsteroids(position: Int) {
        run {
            MaterialAlertDialogBuilder(this)
                .setBackgroundInsetStart(70)
                .setBackgroundInsetEnd(70)
                .setBackgroundInsetTop(10)
                .setBackgroundInsetBottom(100)
                .setBackground(
                    ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
                )
                .setView(R.layout.asteroid_dialog)
                .show()
        }
    }
}