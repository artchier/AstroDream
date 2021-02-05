package com.example.astrodream.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.domain.User
import com.example.astrodream.services.RealtimeUserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class RealtimeViewModel: ViewModel() {

    companion object {
        const val TAG = "===REALTIME_VIEW_MODEL"
    }

    val activeUser = MutableLiveData<User>()

    val realtimeUserRepository = RealtimeUserRepository()

    fun updateUserName(email: String, newName: String) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserName(email, newName)
        }
    }

    fun updateUserNotification(email: String, notifStatus: Boolean) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserNotification(email, notifStatus)
        }
    }

    fun updateUserNasaCoins(email: String, nasaCoins: Long) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserNasaCoins(email, nasaCoins)
        }
    }

    fun updateUserAvatar(email: String, newAvatar: Long) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserAvatar(email, newAvatar)
        }
    }

    fun updateUserListOfAvatar(email: String, avatarList: Map<String, Boolean>) {
        viewModelScope.launch{
            realtimeUserRepository.updateUserListOfAvatar(email, avatarList)
        }
    }

    fun retrieveUserData(email: String, name: String) {
        viewModelScope.launch {
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val userRealtime = dataSnapshot.getValue(User::class.java)
                    // Se usuário inexistente, cria novo usuario
                    if (userRealtime == null) {
                        realtimeUserRepository.addUserRealtime(User(email, name,650))
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
            realtimeUserRepository.retrieveUserRealtime(email, userListener)
        }
    }


}