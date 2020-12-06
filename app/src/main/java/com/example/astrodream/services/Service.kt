package com.example.astrodream.services

import com.example.astrodream.domain.DailyImage
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
    ): DailyImage
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