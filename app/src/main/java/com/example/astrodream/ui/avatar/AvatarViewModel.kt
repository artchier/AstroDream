package com.example.astrodream.ui.avatar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.entitiesDatabase.Avatar
import com.example.astrodream.services.AvatarServiceImplementation
import kotlinx.coroutines.launch

class AvatarViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.invoke(application)
    private val avatarService = AvatarServiceImplementation(db.avatarDAO())

    var listAvatars = MutableLiveData<List<Avatar>>()
    private val _listAvatars: MutableLiveData<List<Avatar>>
        get() = listAvatars

    var lastClickedAvatar = MutableLiveData<Avatar>()

    fun addAllAvatarsTask(avatars: List<Avatar>) {
        viewModelScope.launch {
            avatarService.addAllAvatarsTask(avatars)
        }
    }

    fun getAllAvatarsTask() {
        viewModelScope.launch {
            _listAvatars.value = avatarService.getAllAvatarsTask()
        }
    }

    fun buyAvatarTask(avatar: Avatar) {
        viewModelScope.launch {
            avatarService.buyAvatarTask(avatar)
        }
    }

    fun updateLastClickedAvatarTask(avatar1: Avatar, avatar2: Avatar){
        viewModelScope.launch{
            avatarService.updateLastClickedAvatarTask(avatar1, avatar2)
        }
    }
}