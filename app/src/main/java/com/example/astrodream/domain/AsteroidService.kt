package com.example.astrodream.domain

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Repository{

    @GET("neo/rest/v1/feed")
    suspend fun getResults(
        @Query("start_date")p0: String,
        @Query("api_key")p1: String
    ): AsteroidRes
}

const val urlApiNasa = "https://api.nasa.gov/"

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(urlApiNasa)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val repository: Repository = retrofit.create(Repository::class.java)
