package com.example.astrodream.services

import android.util.Log
import com.example.astrodream.domain.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class RealtimeUserRepository {

    companion object {
        init {FirebaseDatabase.getInstance().setPersistenceEnabled(true)}
    }

    val database = FirebaseDatabase.getInstance()

    private val reference = database.getReference("users")

    fun addUserRealtime(user: User) {
        reference
            .child(user.uid)
            .setValue(user)
    }

    fun updateUserName(uid: String, newName: String) {
        reference
            .child(uid)
            .child("name")
            .setValue(newName)
    }

    fun updateUserNasaCoins(uid: String, nasaCoins: Long) {
        reference
            .child(uid)
            .child("nasaCoins")
            .setValue(nasaCoins)
    }

    fun updateUserAvatar(uid: String, newAvatar: Long) {
        reference
            .child(uid)
            .child("avatar")
            .setValue(newAvatar)
    }

    fun updateUserListOfAvatar(uid: String, avatarList: Map<String, Boolean>) {
        avatarList.forEach { (avatarRes, isBoughtByUser) ->
            reference
                .child(uid)
                .child("avatarList")
                .child(avatarRes)
                .setValue(isBoughtByUser)
        }

    }

    fun retrieveUserRealtime(uid: String, userListener: ValueEventListener) {
        reference.child(uid).addValueEventListener(userListener)
    }

}