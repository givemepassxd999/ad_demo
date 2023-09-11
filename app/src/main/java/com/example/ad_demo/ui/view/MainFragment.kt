package com.example.ad_demo.ui.view

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ad_demo.data.repository.AdRepositoryImpl
import com.example.ad_demo.databinding.FragmentMainBinding
import com.example.ad_demo.network.ApiService
import com.example.ad_demo.network.AppClientManager
import com.example.ad_demo.ui.adapter.ItemAdapter
import com.example.ad_demo.ui.viewmodel.MainViewModel
import com.example.ad_demo.utils.Status
import com.example.ad_sdk.AdSdk

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adViewModel = MainViewModel(
            AdRepositoryImpl(
                AppClientManager.creteRestfulApiClient().create(
                    ApiService::class.java
                )
            )
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        binding.dataList.layoutManager = linearLayoutManager
        val adapter = ItemAdapter()
        binding.dataList.adapter = adapter

        with(adViewModel) {
            fetchNews().observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(it.data)
                        AdSdk.init(activity as MainActivity, binding.dataList, 20)
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getScreenHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}