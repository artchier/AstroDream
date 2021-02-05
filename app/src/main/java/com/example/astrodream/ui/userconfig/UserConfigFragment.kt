package com.example.astrodream.ui.userconfig

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_change_name.view.*
import kotlinx.android.synthetic.main.fragment_user_config.*
import kotlinx.android.synthetic.main.fragment_user_config.view.*

class UserConfigFragment : Fragment() {

    private lateinit var contractUserConfigActivity: ContractUserConfigActivity
    private lateinit var currentUserName: String
    private lateinit var currentUserEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_user_config, container, false)

        contractUserConfigActivity.rvtm.activeUser.observe(viewLifecycleOwner) {
            currentUserName = it.name
            currentUserEmail = it.email
            view.tvUserNameConfig.text = currentUserName
            view.switchNotifications.isChecked = it.notification
        }

        view.btnChangeName.setOnClickListener {
            findNavController().navigate(R.id.action_userConfigFragment_to_userChangeNameFragment)
        }

        view.switchNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
            contractUserConfigActivity.rvtm.updateUserNotification(currentUserEmail, isChecked)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val providerData: List<UserInfo?> = currentUser?.providerData!!
        val providerId = providerData[1]!!.providerId
        if (providerId == "google.com" || providerId == "facebook.com") {
            view.llBtnChangePswd.background.setColorFilter(resources.getColor(R.color.comet), PorterDuff.Mode.SRC_ATOP)
            view.ivBtnChangePswd.setColorFilter(resources.getColor(R.color.transparent_white), PorterDuff.Mode.CLEAR)
            view.btnChangePswd.setTextColor(resources.getColor(R.color.transparent_white))
            view.btnChangePswd.setOnClickListener {
                Toast.makeText(
                    activity,
                    "Essa opção está diponível apenas para login feito com e-mail e senha.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            view.btnChangePswd.setOnClickListener {
                findNavController().navigate(R.id.action_userConfigFragment_to_userChangePasswordFragment)
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContractUserConfigActivity) {
            contractUserConfigActivity = context
        }
    }

}