package com.example.astrodream.services

import android.annotation.SuppressLint
import android.graphics.Bitmap
import com.example.astrodream.domain.PlainClass
import com.google.gson.JsonArray
import com.example.astrodream.domain.Patent
import com.example.astrodream.domain.Software
import com.example.astrodream.domain.Spinoff
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

private const val apikeyMarsAndDaily = "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
private const val apikeyTech = "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
private const val apikeyGlobe = "vX6o8l9GQAr14bmNLonbmLz0Bq2ggLh2wvYfB7C4"

// Endpoints
interface Service {
    /* --------------------------------------- DailyImages -------------------------------------- */
    @GET("planetary/apod")
    suspend fun getDaily(
        @Query("date") date: String,
        @Query("api_key") apikey: String = apikeyMarsAndDaily,
    ): PlainClass

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getMars(
        @Query("earth_date") date: String,
        @Query("api_key") apikey: String = apikeyMarsAndDaily,
    ): JsonObject

    /* ------------------------------------------- Mars ----------------------------------------- */
    @GET("insight_weather/")
    suspend fun getMarsTemp(
        @Query("feedtype") feedtype: String,
        @Query("ver") ver: String,
        @Query("api_key") apikey: String = apikeyMarsAndDaily,
    ): JsonObject


    @GET("neo/rest/v1/feed")
    suspend fun getResults(
        @Query("start_date")p0: String,
        @Query("end_date")p1: String,
        @Query("api_key")p2: String = apikeyMarsAndDaily,
    ): JsonObject

    /* ------------------------------------------- Tech ----------------------------------------- */
    @GET("techtransfer/patent/")
    suspend fun getPatents(
        @Query("api_key") apikey: String = apikeyTech,
    ) : Patent

    @GET("techtransfer/software/")
    suspend fun getSoftwares(
        @Query("api_key") apikey: String = apikeyTech,
    ) : Software

    @GET("techtransfer/spinoff/")
    suspend fun getSpinoffs(
        @Query("api_key") apikey: String = apikeyTech,
    ) : Spinoff

    /* ------------------------------------------ Globe ----------------------------------------- */

    @GET("EPIC/archive/natural/2020/11/17/{extension}/{name}")
    suspend fun getEPIC(
        @Path("extension") extension: String,
        @Path("name") name: String,
        @Query("api_key") apikey: String = apikeyGlobe,
    ): Bitmap

    @GET("EPIC/api/natural/date/{chosenDate}")
    suspend fun getAllEPIC(
        @Path("chosenDate") chosenDate: String,
        @Query("api_key") apikey: String = apikeyGlobe,
    ): JsonArray
}

@SuppressLint("SimpleDateFormat")
fun buildGlobeImageUrl(date: Date, name: String, apikey: String = apikeyGlobe): String =
    "https://api.nasa.gov/EPIC/archive/natural/${
        SimpleDateFormat("yyyy/MM/dd").format(date)
    }/png/${name}.png?api_key=$apikey"

// url
const val urlNasa = "https://api.nasa.gov/"

// Retrofit
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(urlNasa)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Passar instancia do retrofit para o service
val service: Service = retrofit.create(Service::class.java)