package com.example.astrodream.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.User
import com.example.astrodream.services.RealtimeUserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class RealtimeViewModel: ViewModel() {

    val activeUser = MutableLiveData<User>()

    val TAG = "===REALTIME_VIEW_MODEL"

    val realtimeUserRepository = RealtimeUserRepository()

    fun updateUserName(uid: String, newName: String) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserName(uid, newName)
        }
    }

    fun updateUserNasaCoins(uid: String, nasaCoins: Long) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserNasaCoins(uid, nasaCoins)
        }
    }

    fun updateUserAvatar(uid: String, newAvatar: Long) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserAvatar(uid, newAvatar)
        }
    }

    fun updateUserListOfAvatar(uid: String, avatarList: Map<String, Boolean>) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserListOfAvatar(uid, avatarList)
        }
    }

    fun retrieveUserData(uid: String, name: String, email: String) {
        viewModelScope.launch {
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val userRealtime = dataSnapshot.getValue(User::class.java)
                    // Se usuário inexistente, cria novo usuario
                    if (userRealtime == null) {
                        realtimeUserRepository.addUserRealtime(User(uid, name, email, 650))
                    }
                    if (userRealtime != null) {
                        activeUser.value = userRealtime
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            realtimeUserRepository.retrieveUserRealtime(uid, userListener)
        }
    }


}