package com.example.astrodream.ui.userconfig

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.astrodream.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_change_name.view.*

class UserChangeNameFragment : Fragment() {

    private lateinit var contractUserConfigActivity: ContractUserConfigActivity
    private lateinit var user: FirebaseUser
    private lateinit var userUID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_user_change_name, container, false)
        user = Firebase.auth.currentUser!!

        contractUserConfigActivity.rvtm.activeUser.observe(viewLifecycleOwner) {
            val currentUserName = it.name
            view.tiChangeName.setText(currentUserName)
        }

        view.btnConfirmChangeName.setOnClickListener {
            val newName = view.tiChangeName.text.toString()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val providerData: List<UserInfo?> = currentUser?.providerData!!
            val email = providerData[1]!!.email!!
            contractUserConfigActivity.rvtm.updateUserName(email, newName)
            findNavController().popBackStack()
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