package com.example.astrodream.domain

data class MarsImage(
    var sol: Long,
    val camera: Camera,
    val img_src: Any
)
