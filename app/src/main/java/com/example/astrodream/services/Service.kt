package com.example.astrodream.services

import android.annotation.SuppressLint
import com.example.astrodream.domain.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


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
    ): AsteroidRes

    @GET("neo/rest/v1/neo/browse")
    suspend fun getAllAsteroids(
        @Query("api_key") p0: String = apikeyApp
    ): AsteroidAllRes

    @GET("neo/rest/v1/neo/{id}")
    suspend fun getAsteroidId(
        @Path("id") id: Int,
        @Query("api_key") apikey: String = apikeyApp
    ): AsteroidData

    /* ------------------------------------------- Tech ----------------------------------------- */
    @GET("techtransfer/patent/")
    suspend fun getPatents(
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<Patent, JsonObject>

    @GET("techtransfer/software/")
    suspend fun getSoftwares(
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<Software, JsonObject>

    @GET("techtransfer/spinoff/")
    suspend fun getSpinoffs(
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<Spinoff, JsonObject>

    /* ------------------------------------------ Globe ----------------------------------------- */

    @GET("EPIC/api/natural/available")
    suspend fun getAllAvailableEPIC(
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<JsonArray, JsonArray>

    @GET("EPIC/api/natural/date/{chosenDate}")
    suspend fun getAllEPIC(
        @Path("chosenDate") chosenDate: String,
        @Query("api_key") apikey: String = apikeyApp
    ): NetworkResponse<JsonArray, JsonArray>

}

@SuppressLint("SimpleDateFormat")
fun buildGlobeImageUrl(date: String, name: String, apikey: String = apikeyApp): String {
    val parseData = SimpleDateFormat("yyyyMMdd").parse(date)!!
    val dataFormatada = SimpleDateFormat("yyyy/MM/dd").format(parseData)
    return "${urlNasa}EPIC/archive/natural/$dataFormatada/png/$name.png?api_key=$apikey"
}

// url
const val urlNasa = "https://api.nasa.gov/"

// OkHttp
val okHttpClient: OkHttpClient? = OkHttpClient.Builder()
    .readTimeout(60, TimeUnit.SECONDS)
    .connectTimeout(60, TimeUnit.SECONDS)
    .build()

// Retrofit
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(urlNasa)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .client(okHttpClient)
    .build()

// Passar instancia do retrofit para o service
val service: Service = retrofit.create(Service::class.java)