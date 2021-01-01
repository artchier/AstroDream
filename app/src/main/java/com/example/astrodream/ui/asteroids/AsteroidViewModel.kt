package com.example.astrodream.ui.asteroids

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.R
import com.example.astrodream.domain.*
import com.example.astrodream.services.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AsteroidViewModel(val service: Service) : ViewModel() {

    val listResults = MutableLiveData<AsteroidRes>()
    val listAsteroid = ArrayList<Asteroid>()

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun popListResult() {
//        viewModelScope.launch {
//            val listAsteroids =
//                service.getAsteroidsDate(LocalDate.now().toString(), "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M")
//
//            listAsteroids.near_earth_objects.keySet().toList().forEach {
//                val list = Gson().fromJson(listAsteroids.near_earth_objects.get(it),
//                    object : TypeToken<List<AsteroidData>>() {}.type
//                ) as List<AsteroidData>
//                listAsteroid.addAll(list.map { it.getAsteroid() })
//            }
//            listResults.value = listAsteroids
//            Log.i("LIST ASTEROIDS", listAsteroid.map { it.relative_velocity }.toString())
//        }
//    }

    fun execute(v: View) = viewModelScope.launch {
            onPreExecute(v)
            val result = doInBackground()
            onPostExecute(result, v)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private suspend fun doInBackground(): String = withContext(Dispatchers.IO) {
            this.launch {
            Log.i("doInBackground", "do")
                val listAsteroids =
                    service.getAsteroidsDate(LocalDate.now().toString(), "X6SUDXPVkjgyQoyKVunHMpwomboitIigBRVCSK1M")
            Log.i("getAsteroidsDate", "do")

                listAsteroids.near_earth_objects.keySet().toList().forEach {
                    val list = Gson().fromJson(
                        listAsteroids.near_earth_objects.get(it),
                        object : TypeToken<List<AsteroidData>>() {}.type
                    ) as List<AsteroidData>
                    listAsteroid.addAll(list.map { it.getAsteroid() })
                }
                listResults.postValue(listAsteroids)
                Log.i("LIST ASTEROIDS", listAsteroid.map { it.relative_velocity }.toString())
            }
            return@withContext "Executado"
        }

        private fun onPreExecute(v: View) {
            Log.i("onPreExecute", "do")
            showHideProgressBar(true, v)
        }

        private fun onPostExecute(result: String, v: View) {
            Log.i("onPostExecute", "do")
            if (result.equals("Executado")) {
                showHideProgressBar(false, v)
            }
        }

        fun showHideProgressBar(visible: Boolean, v: View) {
                val progressBar: ProgressBar = v.findViewById(R.id.progressbar_asteroides)
                if (visible) progressBar.visibility = ProgressBar.VISIBLE
                else progressBar.visibility = ProgressBar.GONE
    }
}