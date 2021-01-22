package com.example.astrodream.ui.avatar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astrodream.R
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
    private val _lastClickedAvatar: MutableLiveData<Avatar>
        get() = lastClickedAvatar

    fun addAllAvatarsTask() {
        listAvatars.value = listOf(
            Avatar(R.drawable.ic_avatar_normal1, 100, "true", "false"),
            Avatar(R.drawable.ic_avatar_normal2, 100, "true", "false"),
            Avatar(R.drawable.ic_avatar_normal3, 100, "true", "false"),
            Avatar(R.drawable.ic_avatar_normal5, 100, "true", "false"),
            Avatar(R.drawable.ic_avatar_normal4, 200, "true", "false"),
            Avatar(R.drawable.ic_avatar_suit, 200, "true", "false"),
            Avatar(R.drawable.ic_avatar_naked, 300, "true", "false"),
            Avatar(R.drawable.ic_avatar_nuts, 300, "true", "false"),
            Avatar(R.drawable.ic_avatar_alien, 350, "true", "false"),
            Avatar(R.drawable.ic_avatar_astronaut, 350, "true", "false")
        )
        viewModelScope.launch {
            avatarService.addAllAvatarsTask(_listAvatars.value!!)
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

    fun getLastClickedAvatarTask() {
        viewModelScope.launch {
            _lastClickedAvatar.value = avatarService.getLastClickedAvatarTask()
        }
    }

    fun updateLastClickedAvatarTask(newAvatar: Avatar, oldAvatar: Avatar) {
        viewModelScope.launch {
            avatarService.updateLastClickedAvatarTask(newAvatar, oldAvatar)
        }
    }
}