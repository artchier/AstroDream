package com.example.astrodream.ui.dailyimage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.R
import com.example.astrodream.ui.plaindailymars.PlainDetailFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_info_daily.*
import kotlinx.android.synthetic.main.dialog_info_daily.view.*
import kotlinx.android.synthetic.main.fragment_daily.*

class DailyImageFragment : PlainDetailFragment(R.layout.fragment_daily) {

    companion object {
        fun newInstance() = DailyImageFragment()
    }


    override fun popView(view: View) {
        tvTitle.text = plainDetail.title
        Glide.with(view).asBitmap()
            .load(plainDetail.url)
            .into(cvDaily)
        tvDate.text = plainDetail.date

        cvDaily.setOnClickListener {
            if (plainDetail.url is String) { // plainDetail.url is String when API request is successful
                val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
                intent.putExtra("img", plainDetail.url as String)
                startActivity(intent)
            }
        }

        val dialogView = View.inflate(this.requireContext(), R.layout.dialog_info_daily, null)
        dialogView.tvInfoDaily.text = plainDetail.explanation
        val dialog = MaterialAlertDialogBuilder(this.requireContext())
            .setView(dialogView)
            .create()
        btnInfoDaily.setOnClickListener {
            dialogView.btnOk.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

}