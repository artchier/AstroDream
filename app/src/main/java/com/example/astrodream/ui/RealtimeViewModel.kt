package com.example.astrodream.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrodream.R
import com.example.astrodream.domain.User
import com.example.astrodream.services.RealtimeUserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RealtimeViewModel : ViewModel() {
    init {
        animationFadeIn.duration = 500
        animationFadeOut.duration = 500
    }

    companion object {
        const val TAG = "===REALTIME_VIEW_MODEL"
        private val animationFadeIn = AlphaAnimation(0f, 1f)
        private val animationFadeOut = AlphaAnimation(1f, 0f)
        const val dailyNasaCoins = 100
        const val asteroidsNasaCoins = 50
        const val globeNasaCoins = 80
        const val techNasaCoins = 50
        const val marsNasaCoins = 50
    }

    val activeUser = MutableLiveData<User>()

    val realtimeUserRepository = RealtimeUserRepository()

    fun updateUserName(email: String, newName: String) {
        viewModelScope.launch {
            realtimeUserRepository.updateUserName(email, newName)
        }
    }

    fun updateUserNotification(email: String, notifStatus: Boolean) {
        viewModelScope.launch {
            realtimeUserRepository.updateUserNotification(email, notifStatus)
        }
    }

    fun updateUserNasaCoins(email: String, nasaCoins: Long) {
        viewModelScope.launch {
            realtimeUserRepository.updateUserNasaCoins(email, nasaCoins)
        }
    }

    fun updateUserAvatar(email: String, newAvatar: Long) {
        viewModelScope.launch {
            realtimeUserRepository.updateUserAvatar(email, newAvatar)
        }
    }

    fun updateUserListOfAvatar(email: String, avatarList: Map<String, Boolean>) {
        viewModelScope.launch {
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
                        realtimeUserRepository.addUserRealtime(User(email, name, 500))
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

    fun animateNasaCoins(view1: View, view2: TextView, toolbarTitleId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            view1.visibility = VISIBLE
            animationFadeIn.setAnimationListener(object :
                Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    val coinsEarned = when (toolbarTitleId) {
                        R.string.asteroides -> asteroidsNasaCoins
                        R.string.globo -> globeNasaCoins
                        R.string.marte -> marsNasaCoins
                        R.string.tecnologias -> techNasaCoins
                        R.string.daily_image -> dailyNasaCoins
                        else -> 0
                    }
                    val animationValue = ValueAnimator.ofInt(
                        view2.text.toString().toInt(),
                        view2.text.toString().toInt() + coinsEarned
                    )
                    animationValue.duration = 500
                    animationValue.addUpdateListener { animation ->
                        view2.text = animation.animatedValue.toString()
                    }
                    animationValue.start()
                    animationValue.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(500)
                                val fadeOutAnimation = AlphaAnimation(1f, 0f)
                                fadeOutAnimation.duration = 500
                                view1.startAnimation(fadeOutAnimation)
                                view1.visibility = View.INVISIBLE
                            }
                        }

                        override fun onAnimationCancel(p0: Animator?) {}

                        override fun onAnimationRepeat(p0: Animator?) {}
                    })
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            })
            view1.startAnimation(animationFadeIn)
        }
    }
}