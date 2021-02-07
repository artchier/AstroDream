package com.example.astrodream.ui.asteroids

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.domain.Asteroid
import com.example.astrodream.domain.ExpandableListAdapter
import com.example.astrodream.domain.util.*
import com.example.astrodream.entitiesDatabase.AsteroidRoom
import com.example.astrodream.services.ServiceDBAsteroidsImpl
import com.example.astrodream.services.databaseReference
import com.example.astrodream.services.service
import com.example.astrodream.services.shareText
import com.example.astrodream.ui.ActivityWithTopBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_asteroid.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


@Suppress("UNCHECKED_CAST")
class AsteroidActivity : ActivityWithTopBar(R.string.asteroides, R.id.dlAsteroids) {

    private val listAsteroidsButtons = LinkedHashMap<String, ArrayList<Asteroid>>()
    private val listAllAsteroids = mutableSetOf<Asteroid>()
    private val listFourAsteroids = ArrayList<Asteroid>()
    private val expandableListAdapter = ExpandableListAdapter(this)
    private lateinit var listView: ExpandableListView
    private lateinit var navController: NavController
    private val db = AppDatabase.invoke(this)
    private val serviceDB = ServiceDBAsteroidsImpl(db.asteroidDAO())
    private var listButtonsName = ArrayList<String>()

    private val viewModel by viewModels<AsteroidViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AsteroidViewModel(
                    service,
                    serviceDB,
                    databaseReference,
                    this@AsteroidActivity
                ) as T
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asteroid)

        // ##### Set nome dos botões #####
        listButtonsName = arrayListOf(
            getString(R.string.button_1), getString(R.string.button_2),
            getString(R.string.button_3), getString(R.string.button_4)
        )

        // ##### Opções do ExpandableListAdapter #####
        expandableListAdapter.addListButtons(listButtonsName)
        expandableListAdapter.addListAsteroids(listAsteroidsButtons)

        // ##### Opções da listView (botões listar asteroides) #####
        listView = exp_list_view_asteroids
        listView.setAdapter(expandableListAdapter)
        listView.setOnGroupExpandListener { cardview_img_asteroids.visibility = CardView.GONE }
        listView.setOnGroupCollapseListener {
            if (!listView.isSomeGroupExpandad()) cardview_img_asteroids.visibility =
                CardView.VISIBLE
        }
        listView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            when (groupPosition) {
                0, 3 -> onClickAsteroidsDate(childPosition, groupPosition)
                1, 2 -> onClikAsteroidsAll(childPosition, groupPosition)
            }
            false
        }

        if (AstroDreamUtil.isInternetAvailable(this)) {

            // ##### Opções da viewModel #####
            viewModel.getAsteroidsDate()
            viewModel.getListAsteroidesFromFirebase()
            viewModel.getAllAsteroidsDB()

            val progressBar = progressbar_fragment_asteroides
            progressBar.visibility = LinearLayout.VISIBLE

            viewModel.listResultsDateAPI.observe(this) {
                listFourAsteroids.addAll(
                    if (viewModel.listAsteroidsDateAPI.size > 4)
                        viewModel.listAsteroidsDateAPI.subList(0, 4) else emptyList()
                )
                if (viewModel.listAsteroidsDateAPI.size > 4)
                    progressBar.visibility = LinearLayout.GONE
            }

            viewModel.listAllResultsAPI.observe(this) {
                listAllAsteroids.addAll(viewModel.listAllAsteroidsAPI)
            }

            // ##### Opções de navigation da imagem de asteroides #####
            navController = findNavController(R.id.fl_imagem_asteroids)
            val graph = navController.navInflater.inflate(R.navigation.navigation_asteroids)
            val navArgument = NavArgument.Builder().setDefaultValue(listFourAsteroids).build()
            graph.addArgument("listFourAsteroids", navArgument)
            navController.graph = graph

        } else {
            AstroDreamUtil.showErrorInternetConnection(this)
        }
        // ##### Outras opções #####
        setUpMenuBehavior()
    }

    @SuppressLint("InflateParams")
    private fun onClikAsteroidsAll(childPos: Int, groupPos: Int) {
        var asteroid: Asteroid =
            expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[groupPos]]?.get(
                childPos
            ) ?: return
        val progressbar: LinearLayout = progress_bar_activity_asteroides

        progressbar.visibility = LinearLayout.VISIBLE
        viewModel.getOneAsteroById(asteroid.id.toInt())

        val view: View = this.layoutInflater.inflate(R.layout.asteroid_dialog, null)
        view.findViewById<TextView>(R.id.btn_ver_orbita).visibility = TextView.GONE

        viewModel.oneAsteroidFromAPI.observe(this) {
            if (it != null && it.name == asteroid.name) {
                asteroid = it
                onClickAsteroids(asteroid)
                progressbar.visibility = LinearLayout.GONE
                return@observe
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun onClickAsteroids(asteroid: Asteroid) {
        val view: View = this.layoutInflater.inflate(R.layout.asteroid_dialog, null)
        val progressbar: ProgressBar = view.findViewById(R.id.progress_bar_dialog)
        val starFavorites = view.findViewById<View>(R.id.android_favs)
        val shareAsteroidButton = view.findViewById<View>(R.id.ivShareAsteroid)
        val isAsteroidInDB =
            viewModel.listAllAsteroidsDB.map { it.codeAsteroid }.contains(asteroid.name)

        starFavorites.setBackgroundResource(if (isAsteroidInDB) R.drawable.ic_star_filled else R.drawable.ic_star_border)
        progressbar.visibility = ProgressBar.GONE

        progressbar.visibility = ProgressBar.GONE

        if (asteroid.is_potentially_hazardous_asteroid)
            view.findViewById<TextView>(R.id.is_potentially_hazardous_asteroid).visibility =
                TextView.VISIBLE

        val listStrings = arrayOf(
            asteroid.name, asteroid.linkExterno.toString(),
            getString(R.string.dialog_absolute_magnitude, asteroid.absolute_magnitude),
            getString(R.string.dialog_relative_velocity, asteroid.relative_velocity),
            getString(R.string.dialog_close_approach_data, asteroid.close_approach_data),
            getString(R.string.dialog_miss_distance, asteroid.miss_distance),
            getString(R.string.dialog_orbiting_body, asteroid.orbiting_body),
            getString(R.string.dialog_estimated_diameter, asteroid.estimated_diameter)
        )

        view.findViewById<TextView>(R.id.nome_asteroid_dialog).text = listStrings[0]
        view.findViewById<TextView>(R.id.absolute_magnitude).text = listStrings[2]
        view.findViewById<TextView>(R.id.relative_velocity).text = listStrings[3]
        view.findViewById<TextView>(R.id.close_approach_data).text = listStrings[4]
        view.findViewById<TextView>(R.id.miss_distance).text = listStrings[5]
        view.findViewById<TextView>(R.id.orbiting_body).text = listStrings[6]
        view.findViewById<TextView>(R.id.estimated_diameter).text = listStrings[7]

        view.findViewById<TextView>(R.id.btn_ver_orbita).setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(listStrings[1])
            startActivity(i)
        }

        starFavorites.setOnClickListener {
            onAsteroidFavsClickEvent(it, isAsteroidInDB, *listStrings)
        }

        shareAsteroidButton.setOnClickListener {
            if (asteroid.linkExterno == null) {
                Toast.makeText(
                    this,
                    "O asteróide não tem um link externo para compartilhar!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            shareText(
                "Veja informações do asteróide ${asteroid.name}",
                asteroid.linkExterno,
                this
            )
        }

        starFavorites.setOnClickListener {
            onAsteroidFavsClickEvent(it, isAsteroidInDB, *listStrings)
        }

        AstroDreamUtil.showDialogMessage(this, view)
    }


    @SuppressLint("InflateParams")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickAsteroidsDate(childPos: Int, groupPos: Int) {
        val asteroid: Asteroid =
            expandableListAdapter.listAsteroids[expandableListAdapter.listButtons[groupPos]]?.get(
                childPos
            ) ?: return

        onClickAsteroids(asteroid)
    }

    private fun searchAsteroid(view: View) {
        val searchView = view.findViewById<SearchView>(R.id.search_view_asteroid_button)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()

                    val list = ArrayList<Asteroid>()
                    listAllAsteroids.forEach {
                        if (it.name.toLowerCase(Locale.getDefault())
                                .contains(newText.toLowerCase(Locale.getDefault()))
                        ) list.add(it)
                    }

                    expandableListAdapter.listAsteroids[listButtonsName[1]] = list
                    expandableListAdapter.notifyDataSetChanged()
                } else {
                    expandableListAdapter.listAsteroids[listButtonsName[1]]?.clear()
                    expandableListAdapter.listAsteroids[listButtonsName[1]] =
                        listAllAsteroids.toList() as ArrayList<Asteroid>
                    expandableListAdapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showAsteroidCalendar(editText: EditText) {
        val date: LocalDate = LocalDate.now()
        val datePicker = DatePicker((ContextThemeWrapper(this, R.style.DatePicker)), null)

        datePicker.updateDate(date.year, date.monthValue - 1, date.dayOfMonth)

        MaterialAlertDialogBuilder(this)
            .setView(datePicker)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                editText.setText(
                    AstroDreamUtil.formatDate(
                        datePicker.dayOfMonth,
                        datePicker.month + 1,
                        datePicker.year
                    )
                )
            }
            .setNegativeButton(resources.getString(R.string.cancelar), null)
            .show()

        searchAsteroidDate(editText)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun searchAsteroidDate(editText: EditText) {
        val listAsteroidDate = ArrayList<Asteroid>()
        if (editText.text.isNotEmpty()) {
            expandableListAdapter.listAsteroids[listButtonsName[2]]?.clear()

            listAllAsteroids.forEach {
                if (it.close_approach_data == editText.text.toString()) listAsteroidDate.add(
                    it
                )
            }

            expandableListAdapter.listAsteroids[listButtonsName[2]] = listAsteroidDate
            expandableListAdapter.notifyDataSetChanged()
        } else {
            expandableListAdapter.listAsteroids[listButtonsName[2]]?.clear()
            expandableListAdapter.listAsteroids[listButtonsName[2]] =
                listAllAsteroids.toList() as ArrayList<Asteroid>
            expandableListAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onAsteroidFavsClickEvent(view: View, isInDB: Boolean, vararg string: String) {
        val asteroid = getAsteroidRoom(*string)
        if (!isInDB) {
            view.setBackgroundResource(R.drawable.ic_star_filled)
            viewModel.addAsteroidInDB(asteroid)
        } else {
            view.setBackgroundResource(R.drawable.ic_star_border)
            viewModel.deleteAsteroidInDB(asteroid)
        }
    }

    private fun getAsteroidRoom(vararg strings: String): AsteroidRoom {
        val list = strings.toList().subList(2, strings.size).toTypedArray()
        return AsteroidRoom(strings[0], strings[1], AstroDreamUtil.returnTextOf(*list))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onGroupClickEvent(v: View, groupPosition: Int) {
        if (listView.isGroupExpanded(groupPosition)) listView.collapseGroup(groupPosition)
        else listView.expandGroup(groupPosition)

        viewModel.listResultsDateAPI.observe(this) {

            when (groupPosition) {
                0 -> {
                    expandableListAdapter.addListAsteroids(
                        linkedMapOf(
                            expandableListAdapter
                                .listButtons[0] to viewModel.listAsteroidsDateAPI
                        )
                    )
                }
                3 -> {
                    val listPerigosos = ArrayList<Asteroid>()
                    viewModel.listAsteroidsDateAPI.forEach {
                        if (it.is_potentially_hazardous_asteroid) listPerigosos.add(
                            it
                        )
                    }
                    expandableListAdapter.addListAsteroids(
                        linkedMapOf(
                            expandableListAdapter
                                .listButtons[3] to listPerigosos
                        )
                    )
                }
            }
        }
        viewModel.listAllResultsAPI.observe(this) {
            when (groupPosition) {
                1 -> {
                    searchAsteroid(v)
                    expandableListAdapter.addListAsteroids(
                        linkedMapOf(
                            expandableListAdapter
                                .listButtons[1] to if (viewModel.listAllAsteroidsAPI.isEmpty()) arrayListOf(
                                *listAllAsteroids.toTypedArray()
                            )
                            else viewModel.listAllAsteroidsAPI
                        )
                    )
                }
                2 -> {
                    val editText = v.findViewById<EditText>(R.id.et_search_asteroid_date)
                    v.findViewById<ImageView>(R.id.iv_calendar_asteroids)
                        .setOnClickListener { showAsteroidCalendar(editText) }
                    v.findViewById<ImageView>(R.id.iv_searh_date_asteroids)
                        .setOnClickListener { searchAsteroidDate(editText) }
                    expandableListAdapter.addListAsteroids(
                        linkedMapOf(
                            expandableListAdapter
                                .listButtons[2] to if (viewModel.listAllAsteroidsAPI.isEmpty()) arrayListOf(
                                *listAllAsteroids.toTypedArray()
                            )
                            else viewModel.listAllAsteroidsAPI
                        )
                    )
                }
            }
        }
    }
}