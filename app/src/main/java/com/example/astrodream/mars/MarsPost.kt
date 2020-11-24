package com.example.astrodream.mars

data class MarsPost(
    val id: Int,
    var earth_date: String,
    var img_list: ArrayList<String>,
    var maxTemp: String,
    var minTemp: String
)