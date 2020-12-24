package com.example.astrodream.ui.asteroids

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
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
    val list = mutableSetOf<Asteroid>()
    private lateinit var listView: ExpandableListView
    private val expandableListAdapter = ExpandableListAdapter(this)
    private val listButtonsName = arrayListOf(
        "Listar asteroides próximos",
        "Listar asteroides por nome",
        "Listar asteroides por data",
        "Listar asteroides perigosos"
    )

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
            list.addAll(viewModel.listAsteroid)
            val editText = v.findViewById<EditText>(R.id.et_search_asteroid_date)
            searchAsteroid(v)
            viewModel.listResults.observe(this) {
                when (groupPosition) {
                    0 -> {
                        expandableListAdapter.addListAsteroids(linkedMapOf(expandableListAdapter.listButtons[0] to viewModel.listAsteroid))
                    }
                    1 -> {
                        expandableListAdapter.addListAsteroids(linkedMapOf(expandableListAdapter.listButtons[1] to viewModel.listAsteroid))
                    }
                    2 -> {
                        val editText = v.findViewById<EditText>(R.id.et_search_asteroid_date)
                        v.findViewById<ImageView>(R.id.iv_calendar_asteroids).setOnClickListener {
                            showAsteroidCalendar(2020,11,12, editText)
                        }
                        v.findViewById<ImageView>(R.id.iv_searh_date_asteroids).setOnClickListener {
                            searchAsteroidDate(editText)
                        }

                        expandableListAdapter.addListAsteroids(linkedMapOf(expandableListAdapter.listButtons[2] to viewModel.listAsteroid))
                    }
                    3 -> {
                        val listPerigosos = ArrayList<Asteroid>()

                        for (values in list) {
                                if (values.is_potentially_hazardous_asteroid) listPerigosos.add(values)
                            }
                        expandableListAdapter.addListAsteroids(linkedMapOf(expandableListAdapter.listButtons[3] to listPerigosos))
                    }
                }
            }
            false
        }
        setUpMenuBehavior()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickAsteroids(childPos: Int, groupPos: Int) {
        val asteroid =
            expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[groupPos]]?.get(
                childPos
            )
        Log.i("ASTEROID DIALOG", asteroid.toString())
        val li: LayoutInflater = this.layoutInflater
        val view: View = li.inflate(R.layout.asteroid_dialog, null)
        val tx_nome: TextView = view.findViewById(R.id.nome_asteroid_dialog)
        val absolute_magnitude: TextView = view.findViewById(R.id.absolute_magnitude)
        val relative_velocity: TextView = view.findViewById(R.id.relative_velocity)
        val close_approach_data: TextView = view.findViewById(R.id.close_approach_data)
        val miss_distance: TextView = view.findViewById(R.id.miss_distance)
        val orbiting_body: TextView = view.findViewById(R.id.orbiting_body)
        tx_nome.text = asteroid?.name
        absolute_magnitude.text = "Magnitude absoluta: ${asteroid?.absolute_magnitude.toString()}"
        relative_velocity.text = "Velocidade relativa: ${asteroid?.relative_velocity.toString()}"
        close_approach_data.text = "Data de aproximação: ${asteroid?.getDataFormatada()}"
        miss_distance.text = "Distância aproximada: ${asteroid?.miss_distance.toString()}"
        orbiting_body.text = "Órbita:  ${asteroid?.orbiting_body}"

        run {
            MaterialAlertDialogBuilder(this)
                .setBackgroundInsetStart(70)
                .setBackgroundInsetEnd(70)
                .setBackgroundInsetTop(10)
                .setBackgroundInsetBottom(100)
                .setBackground(
                    ContextCompat.getColor(this, android.R.color.transparent).toDrawable()
                )
                .setView(view)
                .show()
        }
    }


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
                        Log.i("asteroid", it.toString())
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun showAsteroidCalendar(year: Int, month: Int, day: Int, editText: EditText){
            val datePicker = DatePicker((ContextThemeWrapper(this, R.style.DatePicker)), null)
            datePicker.updateDate(year, month, day)
            MaterialAlertDialogBuilder(this)
                .setView(datePicker)
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    editText.setText("${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}")
                }
                .setNegativeButton(resources.getString(R.string.cancelar), null)
                .show()
        searchAsteroidDate(editText)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun searchAsteroidDate(editText: EditText) {
      //  val editText: EditText = v.findViewById(R.id.et_search_asteroid_date)

        val listAsteroidDate = ArrayList<Asteroid>()
        if(editText.text.isNotEmpty()) {
            expandableListAdapter.listAsteroids[listButtonsName[2]]?.clear()
            Log.i("EditTEXT", editText.text.toString())
            list.forEach {
                if (it.getDataFormatada() == editText.text.toString()) listAsteroidDate.add(it)
            }
            expandableListAdapter.listAsteroids[listButtonsName[2]] = listAsteroidDate
            Log.i("ListAsteroidDate", listAsteroidDate.toString())
            expandableListAdapter.notifyDataSetChanged()
        } else {
            expandableListAdapter.listAsteroids[listButtonsName[2]]?.clear()
            expandableListAdapter.listAsteroids[listButtonsName[2]] = viewModel.listAsteroid
            expandableListAdapter.notifyDataSetChanged()
        }
    }
}