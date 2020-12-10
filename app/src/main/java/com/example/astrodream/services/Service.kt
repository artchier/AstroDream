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

    //Tech
    @GET("techtransfer/patent/")
    suspend fun getPatents(
        @Query("api_key") apikey: String
    ) : Patent

    @GET("techtransfer/software/")
    suspend fun getSoftwares(
        @Query("api_key") apikey: String
    ) : Software

    @GET("techtransfer/spinoff/")
    suspend fun getSpinoffs(
        @Query("api_key") apikey: String
    ) : Spinoff
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