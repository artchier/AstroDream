package com.example.astrodream.domain

data class PlainClass(
    val title: String = "x",
    val date: String = "x",
    val url: String = "x",
    var earth_date: String = "x",
    var img_list: List<String> = listOf("x"),
    var maxTemp: String = "x",
    var minTemp: String = "x"
)