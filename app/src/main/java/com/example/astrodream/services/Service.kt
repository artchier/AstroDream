package com.example.astrodream.services


import com.example.astrodream.domain.AsteroidRes
import android.annotation.SuppressLint
import android.graphics.Bitmap
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
        @Query("api_key") apikey: String = apikeyApp,
    ): NetworkResponse<PlainClass, DailyErrorResponse>

    /* ------------------------------------------- Mars ----------------------------------------- */
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getMars(
        @Query("earth_date") date: String,
        @Query("api_key") apikey: String = apikeyApp,
    ): NetworkResponse<JsonObject, JsonObject>

    @GET("insight_weather/")
    suspend fun getMarsTemp(
        @Query("feedtype") feedtype: String,
        @Query("ver") ver: String,
        @Query("api_key") apikey: String = apikeyApp,
    ): NetworkResponse<JsonObject, JsonObject>

    /* ------------------------------------------- Asteroid ------------------------------------- */
    @GET("neo/rest/v1/feed")
    suspend fun getResults(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apikey: String = apikeyApp,
    ): JsonObject

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsDate(
        @Query("start_date")p0: String,
        @Query("api_key")p1: String
    ): AsteroidRes

    @GET("neo/rest/v1/neo/browse")
    suspend fun getAllAsteroids(
        @Query("api_key")p0: String,
    ): AsteroidRes

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidId(
        @Query("start_date")p0: String,
        @Query("api_key")p1: String,
        @Query("end_date")p2: String
    ): AsteroidRes

    /* ------------------------------------------- Tech ----------------------------------------- */
    @GET("techtransfer/patent/")
    suspend fun getPatents(
        @Query("api_key") apikey: String = apikeyApp,
    ): Patent

    @GET("techtransfer/software/")
    suspend fun getSoftwares(
        @Query("api_key") apikey: String = apikeyApp,
    ): Software

    @GET("techtransfer/spinoff/")
    suspend fun getSpinoffs(
        @Query("api_key") apikey: String = apikeyApp,
    ): Spinoff

    /* ------------------------------------------ Globe ----------------------------------------- */

    @GET("EPIC/archive/natural/2020/11/17/{extension}/{name}")
    suspend fun getEPIC(
        @Path("extension") extension: String,
        @Path("name") name: String,
        @Query("api_key") apikey: String = apikeyApp,
    ): Bitmap

    @GET("EPIC/api/natural/date/{chosenDate}")
    suspend fun getAllEPIC(
        @Path("chosenDate") chosenDate: String,
        @Query("api_key") apikey: String = apikeyApp,
    ): JsonArray

}

@SuppressLint("SimpleDateFormat")
fun buildGlobeImageUrl(date: Date, name: String, apikey: String = apikeyApp): String {
    val dataFormatada = SimpleDateFormat("yyyy/MM/dd").format(date)

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
val service: Service = retrofit.create(Service::class.java)