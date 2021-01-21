package com.example.astrodream.ui.dailyimage

import android.content.Context
import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.R
import com.example.astrodream.services.cancelWallpaperChange
import com.example.astrodream.services.scheduleWallpaperChange
import com.example.astrodream.ui.plaindailymars.PlainDetailFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_info_daily.view.*
import kotlinx.android.synthetic.main.fragment_daily.view.*

class DailyImageFragment : PlainDetailFragment(R.layout.fragment_daily) {

    companion object {
        fun newInstance() = DailyImageFragment()
    }

    override fun popView(view: View) {
        val img = if (plainDetail.url != "") { plainDetail.url } else { R.drawable.no_internet }

        view.tvTitle.text = plainDetail.title

        Glide.with(view).asBitmap()
            .load(img)
            .into(view.cvDaily)

        view.tvDate.text = plainDetail.date

        view.cvDaily.setOnClickListener {
            if (plainDetail.url != "") { // plainDetail.url is not empty String when API request is successful
                val intent = Intent(view.context, FullScreenImgActivity::class.java)
                intent.putExtra("img", plainDetail.url)
                intent.putExtra("hdimg", plainDetail.hdurl)
                startActivity(intent)
            }
        }

        val dialogView = View.inflate(this.requireContext(), R.layout.dialog_info_daily, null)

        dialogView.tvInfoDaily.text = plainDetail.explanation

        val dialog = MaterialAlertDialogBuilder(this.requireContext())
            .setView(dialogView)
            .create()

        view.btnInfoDaily.setOnClickListener {
            if (plainDetail.explanation != "") { // plainDetail.explanation is not empty String when API request is successful
                dialogView.btnOk.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }

        val prefs = requireActivity().getSharedPreferences("wallpaper_job", Context.MODE_PRIVATE)
        val scheduled = prefs.getBoolean("scheduled", false)

        view.checkDaily.isChecked = scheduled

        view.checkDaily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scheduleWallpaperChange(requireContext())
            }
            else {
                cancelWallpaperChange(requireContext())
            }

            prefs.edit().putBoolean("scheduled", isChecked).apply()
        }
    }
}