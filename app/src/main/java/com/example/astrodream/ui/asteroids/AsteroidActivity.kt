package com.example.astrodream.ui.asteroids

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.astrodream.R
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.domain.ExpandableListAdapter
import com.example.astrodream.services.service
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_asteroid.*
import java.util.LinkedHashMap

class AsteroidActivity : ActivityWithTopBar(R.string.asteroides, R.id.dlAsteroids) {
    val list = ArrayList<Asteroid>()
    private lateinit var listView: ExpandableListView
    private val expandableListAdapter = ExpandableListAdapter(this)
    private val listButtonsName = arrayListOf(
        "Listar asteroides próximos",
        "Listar asteroides por nome",
        "Listar asteroides por data",
        "Listar asteroides perigosos"
    )
    private var displayList = ArrayList<Asteroid>()

    private val listAsteroids = LinkedHashMap<String, ArrayList<Asteroid>>()
    private lateinit var navController: NavController

    val viewModel by viewModels<AsteroidViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AsteroidViewModel(service) as T
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid)

        navController = findNavController(R.id.fl_imagem_asteroids)
        listView = exp_list_view_asteroids
        listView.setAdapter(expandableListAdapter)
        expandableListAdapter.addListButtons(listButtonsName)
        expandableListAdapter.addListAsteroids(listAsteroids)
        viewModel.popListResult()

        listView.setOnChildClickListener { parent, view, groupPosition, childPosition, id ->
            onClickAsteroids(childPosition, groupPosition)
            false
        }

        listView.setOnGroupClickListener { parent, v, groupPosition, id ->
            searchAsteroid(v)
            viewModel.listResults.observe(this) {
                when (groupPosition) {
                    0 -> {
                        expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[0]] =
                            viewModel.listAsteroid
                    }
                    1 -> {
                        expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[1]] =
                            viewModel.listAsteroid
                        list.addAll(viewModel.listAsteroid)
                    }
                    2 -> {
                        expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[2]] =
                            viewModel.listAsteroid
                    }
                    3 -> {
                        val list = ArrayList<Asteroid>()
                        for (values in listAsteroids) {
                            values.value.forEach {
                                if (it.is_potentially_hazardous_asteroid) list.add(it)
                            }
                        }
                        expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[3]] = list
                    }
                }
            }
            false
        }
        setUpMenuBehavior()
    }

    private fun onClickAsteroids(childPos: Int, groupPos: Int) {
        val asteroid =
            expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[groupPos]]?.get(
                childPos
            )
        val li: LayoutInflater = this.layoutInflater
        val view: View = li.inflate(R.layout.asteroid_dialog, null)
        val tx_nome: TextView = view.findViewById(R.id.nome_asteroid_dialog)
        val absolute_magnitude: TextView = view.findViewById(R.id.absolute_magnitude)
        val relative_velocity: TextView = view.findViewById(R.id.relative_velocity)
        val close_approach_data: TextView = view.findViewById(R.id.close_approach_data)
        val miss_distance: TextView = view.findViewById(R.id.miss_distance)
        val orbiting_body: TextView = view.findViewById(R.id.orbiting_body)
        tx_nome.text = asteroid?.name
        absolute_magnitude.text = asteroid?.absolute_magnitude.toString()
        relative_velocity.text = asteroid?.relative_velocity.toString()
        close_approach_data.text = asteroid?.date
        miss_distance.text = asteroid?.miss_distance.toString()
        orbiting_body.text = asteroid?.orbiting_body

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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_asteroids, menu)
//        val searchItem = menu?.findItem(R.id.menu_search_asteroid)
//        if (searchItem != null){
//            val searchView = searchItem.actionView as
//                    SearchView
//            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
//            editText.hint = "Digite o nome do asteroide..."
//
//            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    Log.i("newText", newText.toString())
//                    if(newText!!.isNotEmpty()){
//                        val list = expandableListAdapter.listAsteroids[listButtonsName[1]]
//                        Log.i("list", list.toString())
//                        expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()
//                        Log.i("listalimpa", expandableListAdapter.listAsteroids[listButtonsName[1]].toString())
//                        val search = newText.toLowerCase()
//                        list?.forEach {
//                            var name = it.name
//                            Log.i("name", name)
//                            val list2 = ArrayList<Asteroid>()
//                            if(name.toLowerCase().contains(search)) list2.add(it)
//                            Log.i("listatualizada", list.toString())
//                            expandableListAdapter.listAsteroids[listButtonsName[1]] = list2
//                        }
//                        expandableListAdapter.notifyDataSetChanged()
//                    } else {
//                        expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()
//                        expandableListAdapter.listAsteroids[listButtonsName[1]] = viewModel.listAsteroid
//                        expandableListAdapter.notifyDataSetChanged()
//                    }
//                    return true
//                }
//            })
//        }
//        return true
//    }

    fun searchAsteroid(view: View){
        val searchView = view.findViewById<SearchView>(R.id.search_view_asteroid_button)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("newText", newText.toString())
                if(newText!!.isNotEmpty()){
                    Log.i("list", list.toString())
                    expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()
                    Log.i("listalimpa", expandableListAdapter.listAsteroids[listButtonsName[1]].toString())
                    val search = newText.toLowerCase()
                    val list2 = ArrayList<Asteroid>()
                    list?.forEach {
                        var name = it.name
                        Log.i("name", name)
                        if(name.toLowerCase().contains(search)) list2.add(it)
                        Log.i("listatualizada", list2.toString())
                    }
                    expandableListAdapter.listAsteroids[listButtonsName[1]] = list2
                    expandableListAdapter.notifyDataSetChanged()
                } else {
                    expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()
                    expandableListAdapter.listAsteroids[listButtonsName[1]] = viewModel.listAsteroid
                    expandableListAdapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_asteroids, menu)
//        val searchItem = menu?.findItem(R.id.menu_search_asteroid)
//        if (searchItem != null){
//            val searchView = searchItem.actionView as
//                    SearchView
//            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
//            editText.hint = "Digite o nome do asteroide..."
//
//            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    Log.i("newText", newText.toString())
//                    if(newText!!.isNotEmpty()){
//                        Log.i("list", list.toString())
//                        expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()
//                        Log.i("listalimpa", expandableListAdapter.listAsteroids[listButtonsName[1]].toString())
//                        val search = newText.toLowerCase()
//                        list?.forEach {
//                            var name = it.name
//                            Log.i("name", name)
//                            val list2 = ArrayList<Asteroid>()
//                            if(name.toLowerCase().contains(search)) list2.add(it)
//                            Log.i("listatualizada", list.toString())
//                            expandableListAdapter.listAsteroids[listButtonsName[1]] = list2
//                        }
//                        expandableListAdapter.notifyDataSetChanged()
//                    } else {
//                        expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()
//                        expandableListAdapter.listAsteroids[listButtonsName[1]] = viewModel.listAsteroid
//                        expandableListAdapter.notifyDataSetChanged()
//                    }
//                    return true
//                }
//            })
//        }
}