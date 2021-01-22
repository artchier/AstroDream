package com.example.astrodream.ui.dailyimage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.R
import com.example.astrodream.database.AppDatabase
import com.example.astrodream.entitiesDatabase.DailyRoom
import com.example.astrodream.entitiesDatabase.MarsRoom
import com.example.astrodream.services.ServiceDBDaily
import com.example.astrodream.services.ServiceDBImplementationDaily
import com.example.astrodream.services.service
import com.example.astrodream.ui.plaindailymars.PlainActivity
import com.example.astrodream.ui.plaindailymars.PlainActivityType
import com.example.astrodream.services.cancelWallpaperChange
import com.example.astrodream.services.scheduleWallpaperChange
import com.example.astrodream.ui.plaindailymars.PlainDetailFragment
import com.example.astrodream.ui.plaindailymars.PlainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_info_daily.view.*
import kotlinx.android.synthetic.main.fragment_daily.view.*
import kotlinx.android.synthetic.main.fragment_recent_mars.view.*

class DailyImageFragment : PlainDetailFragment(R.layout.fragment_daily) {

    companion object {
        fun newInstance() = DailyImageFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextActivity = this.requireActivity()

        view.btnFavDaily.setOnClickListener{
            if(contextActivity is PlainActivity) {
                val viewModel: PlainViewModel by activityViewModels()
                viewModel.favPlainDB(plainDetail, it as ToggleButton, requireActivity())
            } else {
                val db = AppDatabase.invoke(contextActivity)
                val repositoryDaily = ServiceDBImplementationDaily(db.dailyDAO())
                val viewModel by viewModels<PlainViewModel> {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                            return PlainViewModel(service, PlainActivityType.DailyImage, repositoryDaily) as T
                        }
                    }
                }
                viewModel.favPlainDB(plainDetail, it as ToggleButton, requireActivity())
            }
        }
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

        view.btnFavDaily.isChecked = plainDetail.isFav
//        viewModel.favDailyState(DailyRoom(plainDetail.title, plainDetail.date, plainDetail.explanation), view.btnFavDaily)

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
    }

            prefs.edit().putBoolean("scheduled", isChecked).apply()
        }
    }
}