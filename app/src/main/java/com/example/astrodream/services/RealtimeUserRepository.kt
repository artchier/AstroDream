package com.example.astrodream.services

import com.example.astrodream.domain.User
import com.example.astrodream.domain.util.toMD5
import com.google.firebase.database.*

class RealtimeUserRepository {

    companion object {
        init {FirebaseDatabase.getInstance().setPersistenceEnabled(true)}
    }

    val database = FirebaseDatabase.getInstance()

    private val reference = database.getReference("users")

    fun addUserRealtime(user: User) {
        reference
            .child(user.email.toMD5())
            .setValue(user)
    }

    fun updateUserName(email: String, newName: String) {
        reference
            .child(email.toMD5())
            .child("name")
            .setValue(newName)
    }

    fun updateUserNotification(email: String, notifStatus: Boolean) {
        reference
            .child(email.toMD5())
            .child("notification")
            .setValue(notifStatus)
    }

    fun updateUserNasaCoins(email: String, nasaCoins: Long) {
        reference
            .child(email.toMD5())
            .child("nasaCoins")
            .setValue(nasaCoins)
    }

    fun updateUserAvatar(email: String, newAvatar: Long) {
        reference
            .child(email.toMD5())
            .child("avatar")
            .setValue(newAvatar)
    }

    fun updateUserListOfAvatar(email: String, avatarList: Map<String, Boolean>) {
        avatarList.forEach { (avatarRes, isBoughtByUser) ->
            reference
                .child(email.toMD5())
                .child("avatarList")
                .child(avatarRes)
                .setValue(isBoughtByUser)
        }

    }

    fun retrieveUserRealtime(email: String, userListener: ValueEventListener) {
        reference.child(email.toMD5()).addValueEventListener(userListener)
    }

}