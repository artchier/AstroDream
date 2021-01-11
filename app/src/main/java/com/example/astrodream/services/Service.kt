package com.example.astrodream.services

import android.graphics.Bitmap
import com.example.astrodream.domain.PlainClass
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Endpoint
interface Service {
    @GET("planetary/apod")
    suspend fun getDaily(
        @Query("date") date: String,
        @Query("api_key") apikey: String,
    ): PlainClass

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getMars(
        @Query("earth_date") date: String,
        @Query("api_key") apikey: String,
    ): JsonObject

    @GET("EPIC/archive/natural/2020/11/17/{extension}/{name}?api_key=vX6o8l9GQAr14bmNLonbmLz0Bq2ggLh2wvYfB7C4")
    suspend fun getEPIC(
        @Path("extension") extension: String,
        @Path("name") name: String
    ): Bitmap

    @GET("EPIC/api/natural/date/{chosenDate}?api_key=$apikeyApp")
    suspend fun getAllEPIC(
        @Path("chosenDate") chosenDate: String
    ): JsonArray
}

// url
val urlNasa = "https://api.nasa.gov/"

// Retrofit
val retrofit = Retrofit.Builder()
    .baseUrl(urlNasa)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Passar instancia do retrofit para o service
val service: Service = retrofit.create(Service::class.java)