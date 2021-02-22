package com.example.astrodream.domain

import com.example.astrodream.R

data class User(
    var email: String = "",
    var name: String = "",
    var nasaCoins: Long = 0,
    var avatar: Int = R.drawable.avatar_male_01,
    var avatarList: Map<String, Boolean> = mutableMapOf(),
    var notification: Boolean = true
) {
    
    init {
        // Map <res.id, userBoughtItAlready>
        avatarList = mutableMapOf(
            R.drawable.avatar_male_01.toString() to true,
            R.drawable.avatar_female_01.toString() to true,
            R.drawable.avatar_male_02.toString() to false,
            R.drawable.avatar_female_02.toString() to false,
            R.drawable.avatar_male_03.toString() to false,
            R.drawable.avatar_female_03.toString() to false,
            R.drawable.avatar_male_05.toString() to false,
            R.drawable.avatar_female_04.toString() to false,
            R.drawable.avatar_male_04.toString() to false,
            R.drawable.avatar_female_05.toString() to false,
            R.drawable.avatar_male_06.toString() to false,
            R.drawable.avatar_female_06.toString() to false,
            R.drawable.avatar_male_07.toString() to false,
            R.drawable.avatar_female_07.toString() to false,
            R.drawable.avatar_male_08.toString() to false,
            R.drawable.avatar_female_08.toString() to false,
            R.drawable.avatar_male_09.toString() to false,
            R.drawable.avatar_female_09.toString() to false,
            R.drawable.avatar_male_10.toString() to false,
            R.drawable.avatar_female_10.toString() to false
        )
    }
}