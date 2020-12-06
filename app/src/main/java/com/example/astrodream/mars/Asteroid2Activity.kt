package com.example.astrodream.mars

import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.Toast
import com.example.astrodream.ActivityWithTopBar
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroids
import com.example.astrodream.domain.AsteroidsAdapter
import com.example.astrodream.domain.AsteroidsAdapter.OnClickAsteroidsListener
import com.example.astrodream.domain.ExpandableListAdapter
import com.sysdata.widget.accordion.ItemAdapter.OnItemClickedListener
import kotlinx.android.synthetic.main.activity_asteroid2.*


class Asteroid2Activity : ActivityWithTopBar(R.string.asteroides, R.id.dlAsteroids2), OnClickAsteroidsListener {
    lateinit var listView: ExpandableListView
    val expandableListAdapter = ExpandableListAdapter(this)
    val listButtonsName = ArrayList<String>()
    val listAsteroids = HashMap<String, ArrayList<Asteroids>>()

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

    override fun onClickAsteroids(viewHolder: AsteroidsAdapter.AsteroidsViewHolder, position: Int) {
        val itemHolder = viewHolder.itemView
        when (position) {
            OnItemClickedListener.ACTION_ID_COLLAPSED_VIEW -> Toast.makeText(this, "clicado", Toast.LENGTH_SHORT).show()
            OnItemClickedListener.ACTION_ID_EXPANDED_VIEW -> Toast.makeText(this, "desclicado", Toast.LENGTH_SHORT).show()
            else -> {
            }
    }
    }

}