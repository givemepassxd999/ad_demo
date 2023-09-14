package com.example.ad_demo.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ad_demo.data.repository.AdRepositoryImpl
import com.example.ad_demo.databinding.FragmentMainBinding
import com.example.ad_demo.network.ApiService
import com.example.ad_demo.network.AppClientManager
import com.example.ad_demo.ui.adapter.ItemAdapter
import com.example.ad_demo.ui.viewmodel.MainViewModel
import com.example.ad_demo.utils.Status
import com.example.ad_sdk.ad.AdData
import com.example.ad_sdk.ad.AdLoader
import com.example.ad_sdk.ad.OnAdListener
import com.example.ad_sdk.ad.OnAdLoadedListener

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
        activity?.let { aty ->
            AdLoader.Companion.Builder(activity = aty)
                .init(binding.dataList, 20)
                .forAd(object : OnAdLoadedListener {
                    override fun onAdData(adData: AdData) {
                        //update ad data to list
                    }

                })
                .withAdListener(object : OnAdListener {
                    override fun onAdImpression() {

                    }

                })
                .build()
        }
        with(adViewModel) {
            fetchNews().observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(it.data)
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

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}