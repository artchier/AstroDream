package com.example.astrodream.ui.avatar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.domain.Avatar
import com.example.astrodream.domain.User
import com.example.astrodream.entitiesDatabase.AvatarRoom
import com.example.astrodream.services.AvatarServiceImplementation
import com.example.astrodream.services.RealtimeUserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AvatarViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.invoke(application)
    private val avatarService = AvatarServiceImplementation(db.avatarDAO())

    var listAvatars = MutableLiveData<List<Avatar>>()

    val realtimeUserRepository = RealtimeUserRepository()
    var listAvatarsRealtime = MutableLiveData<Map<String, Boolean>>()
    var currentAvatarRealtime = MutableLiveData<Int>()

    var listAvatarsRoom = MutableLiveData<List<AvatarRoom>>()
    private val _listAvatarsRoom: MutableLiveData<List<AvatarRoom>>
        get() = listAvatarsRoom

    fun initAllAvatarsAtRoom() {
        listAvatarsRoom.value = listOf(
            AvatarRoom(R.drawable.avatar_male_01, 0),
            AvatarRoom(R.drawable.avatar_female_01, 0),
            AvatarRoom(R.drawable.avatar_male_02, 1000),
            AvatarRoom(R.drawable.avatar_female_02, 1000),
            AvatarRoom(R.drawable.avatar_male_03, 1000),
            AvatarRoom(R.drawable.avatar_female_03, 1000),
            AvatarRoom(R.drawable.avatar_male_05, 1000),
            AvatarRoom(R.drawable.avatar_female_04, 1000),
            AvatarRoom(R.drawable.avatar_male_04, 2000),
            AvatarRoom(R.drawable.avatar_female_05, 2000),
            AvatarRoom(R.drawable.avatar_male_06, 2000),
            AvatarRoom(R.drawable.avatar_female_06, 2000),
            AvatarRoom(R.drawable.avatar_male_07, 3000),
            AvatarRoom(R.drawable.avatar_female_07, 3000),
            AvatarRoom(R.drawable.avatar_male_08, 3000),
            AvatarRoom(R.drawable.avatar_female_08, 3000),
            AvatarRoom(R.drawable.avatar_male_09, 3500),
            AvatarRoom(R.drawable.avatar_female_09, 3500),
            AvatarRoom(R.drawable.avatar_male_10, 3500),
            AvatarRoom(R.drawable.avatar_female_10, 3500),
            AvatarRoom(R.drawable.avatar_nasare, 10000)
        )
        viewModelScope.launch {
            avatarService.addAllAvatarsTask(_listAvatarsRoom.value!!)
        }
    }

    fun getAllAvatarsFromRoom() {
        viewModelScope.launch {
            _listAvatarsRoom.value = avatarService.getAllAvatarsTask()
        }
    }

    fun retrieveUserAvatarData(email: String) {
        viewModelScope.launch {
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val userRealtime = dataSnapshot.getValue(User::class.java)
                    listAvatarsRealtime.value = userRealtime?.avatarList
                    currentAvatarRealtime.value = userRealtime?.avatar
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("===================", "loadPost:onCancelled", databaseError.toException())
                }
            }
            realtimeUserRepository.retrieveUserRealtime(email, userListener)
        }
    }

    fun mergeAvatarDataRoomRealtime() {
        viewModelScope.launch {
            val listPrelim = mutableListOf<Avatar>()

            listAvatars.value =
                withContext(Dispatchers.Default) {
                    var avatarRes: Int
                    var avatarPrice: Int
                    var avatarBoughtByUser: Boolean
                    var avatarCurrent: Boolean

                    _listAvatarsRoom.value?.forEach {
                        avatarRes = it.avatarRes
                        avatarPrice = it.price
                        avatarBoughtByUser =
                            listAvatarsRealtime.value?.get(it.avatarRes.toString()) ?: false
                        avatarCurrent = it.avatarRes == currentAvatarRealtime.value
                        listPrelim.add(
                            Avatar(
                                avatarRes,
                                avatarPrice,
                                avatarBoughtByUser,
                                avatarCurrent
                            )
                        )
                    }
                    return@withContext listPrelim
                }
        }
    }

}