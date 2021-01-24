package com.example.astrodream.domain

data class PlainClass(
    val title: String = "",
    var date: String = "",
    val url: String = "",
    val hdurl: String = "",
    val explanation: String = "",
    var earth_date: String = "",
    var sol: Long = 1,
    var img_list: List<MarsImage> = listOf(MarsImage(1, Camera("", ""), "")),
    var maxTemp: String = "Temperaturas",
    var minTemp: String = "indisponiveis",
    var isFav: Boolean = false
)
