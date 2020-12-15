package com.example.astrodream.services

import com.example.astrodream.domain.AsteroidRes
import com.example.astrodream.domain.PlainClass
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val apikey = "k070HGqyd0nQeVXvDaMsWeW4Q1aWernx6N4UDsDj"

// Endpoints
interface Service {
    @GET("planetary/apod")
    suspend fun getDaily(
        @Query("date") date: String,
        @Query("api_key") api_key: String = apikey,
    ): PlainClass

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getMars(
        @Query("earth_date") date: String,
        @Query("api_key") api_key: String = apikey,
    ): JsonObject

    @GET("insight_weather/")
    suspend fun getMarsTemp(
        @Query("feedtype") feedtype: String,
        @Query("ver") ver: String,
        @Query("api_key") api_key: String = apikey,
    ): JsonObject

    @GET("neo/rest/v1/feed")
    suspend fun getResults(
        @Query("start_date")p0: String,
        @Query("end_date")p1: String,
        @Query("api_key")p2: String = apikey
    ): JsonObject

    // -----------asteroids-------------
    @GET("neo/rest/v1/feed?")
    suspend fun getAsteroids(
        @Query("start_date")p0: String,
        @Query("api_key")p1: String,
        @Query("end_date")p2: String = "2020-12-12"
    ): AsteroidRes
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