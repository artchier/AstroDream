package com.example.astrodream.domain

data class Avatar(
    val avatarRes: Int,
    val price: Int,
    var isBoughtByCurrentUser: Boolean,
    var isCurrent: Boolean
)