package com.example.astrodream.services

import com.example.astrodream.domain.AsteroidRes
import android.annotation.SuppressLint
import android.util.Log
import com.example.astrodream.domain.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

// Endpoints
interface Service {
    /* --------------------------------------- DailyImages -------------------------------------- */
    @GET("planetary/apod")
    suspend fun getDaily(
        @Query("date") date: String,
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<PlainClass, DailyErrorResponse>

    /* ------------------------------------------- Mars ----------------------------------------- */
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getMars(
        @Query("earth_date") date: String,
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<JsonObject, JsonObject>

    @GET("insight_weather/")
    suspend fun getMarsTemp(
        @Query("feedtype") feedtype: String,
        @Query("ver") ver: String,
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<JsonObject, JsonObject>

    /* ------------------------------------------- Asteroid ------------------------------------- */
    @GET("neo/rest/v1/feed")
    suspend fun getResults(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apikey: String = apikeyApp
    ): JsonObject

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsDate(
        @Query("start_date") startDate: String,
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<AsteroidRes, AsteroidsErrorResponse>

    @GET("neo/rest/v1/neo/browse")
    suspend fun getAllAsteroids(
        @Query("api_key")p0: String = apikeyApp
    ): AsteroidAllRes

    @GET("neo/rest/v1/neo/{id}")
    suspend fun getAsteroidId(
        @Path("id") id: Int,
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<AsteroidData, AsteroidsErrorResponse>

    /* ------------------------------------------- Tech ----------------------------------------- */
    @GET("techtransfer/patent/")
    suspend fun getPatents(
        @Query("api_key") apikey: String = apikeyApp
    ): Patent

    @GET("techtransfer/software/")
    suspend fun getSoftwares(
        @Query("api_key") apikey: String = apikeyApp
    ): Software

    @GET("techtransfer/spinoff/")
    suspend fun getSpinoffs(
        @Query("api_key") apikey: String = apikeyApp
    ): Spinoff

    /* ------------------------------------------ Globe ----------------------------------------- */

    @GET("EPIC/api/natural/available")
    suspend fun getAllAvailableEPIC(
        @Query("api_key") apikey: String = apikeyApp
    ): JsonArray

    @GET("EPIC/api/natural/date/{chosenDate}")
    suspend fun getAllEPIC(
        @Path("chosenDate") chosenDate: String,
        @Query("api_key") apikey: String = apikeyApp
    ): JsonArray

}

@SuppressLint("SimpleDateFormat")
fun buildGlobeImageUrl(date: String, name: String, apikey: String = apikeyApp): String {
    val parseData = SimpleDateFormat("yyyyMMdd").parse(date)!!
    val dataFormatada = SimpleDateFormat("yyyy/MM/dd").format(parseData)
    return "${urlNasa}EPIC/archive/natural/$dataFormatada/png/$name.png?api_key=$apikey"
}

// url
const val urlNasa = "https://api.nasa.gov/"

// Retrofit
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(urlNasa)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .build()

// Passar instancia do retrofit para o service
const val apikeyApp = "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
val service: Service = retrofit.create(Service::class.java)