package com.example.astrodream.services

import com.example.astrodream.domain.Patent
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.domain.Software
import com.example.astrodream.domain.Spinoff
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val apikeyMarsAndDaily = "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"
private const val apikeyTech = "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"

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
}

// url
const val urlNasa = "https://api.nasa.gov/"

// Retrofit
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(urlNasa)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Passar instancia do retrofit para o service
val service: Service = retrofit.create(Service::class.java)