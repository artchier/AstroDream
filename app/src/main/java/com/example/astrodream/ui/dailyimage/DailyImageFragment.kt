package com.example.astrodream.ui.dailyimage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.astrodream.ui.FullScreenImgActivity
import com.example.astrodream.R
import com.example.astrodream.domain.DailyImage
import com.example.astrodream.services.service
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.fragment_daily.view.*

class DailyImageFragment : Fragment() {

    companion object {
        fun newInstance() = DailyImageFragment()
    }

    private val viewModel : DailyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_daily, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("DailyImageFrag", "onViewCReated =====================================")
        val dailyPic = viewModel.listResults.value!![0]

        tvTitle.text = dailyPic.title
        Glide.with(view).asBitmap()
            .load(dailyPic.url)
            .into(ivDaily)
        tvDate.text = dailyPic.date

        ivDaily.setOnClickListener {
            val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
            intent.putExtra("img", dailyPic.url)
            startActivity(intent)
        }

        viewModel.focusResult.observe(viewLifecycleOwner, Observer<DailyImage> { daily ->
            Log.i("DailyImageFrag", "onViewCReated OBSERVE =====================================")
            tvTitle.text = daily.title
            Glide.with(this).asBitmap()
                .load(daily.url)
                .into(ivDaily)
            tvDate.text = daily.date

            ivDaily.setOnClickListener {
                val intent: Intent = Intent(view.context, FullScreenImgActivity::class.java)
                intent.putExtra("img", daily.url)
                startActivity(intent)
            }
        })
    }



}