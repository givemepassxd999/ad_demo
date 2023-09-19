package com.example.ad_demo.ui.view

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ad_demo.R
import com.example.ad_demo.data.repository.AdRepositoryImpl
import com.example.ad_demo.databinding.CreateItemBinding
import com.example.ad_demo.databinding.FragmentMainBinding
import com.example.ad_demo.network.ApiService
import com.example.ad_demo.network.AppClientManager
import com.example.ad_demo.ui.adapter.ItemAdapter
import com.example.ad_demo.ui.viewmodel.MainViewModel
import com.example.ad_demo.utils.Status
import com.example.ad_demo.utils.ViewHolderType
import com.example.ad_sdk.ad.AdData
import com.example.ad_sdk.ad.AdLoader
import com.example.ad_sdk.ad.OnAdListener
import com.example.ad_sdk.ad.OnAdLoadedListener

class MainFragment : Fragment() {

    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var adViewModel: MainViewModel
    private val adapter = ItemAdapter()
    private val adList = arrayListOf<AdLoader>()
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
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(fragmentMainBinding) {
            val linearLayoutManager = LinearLayoutManager(context)
            dataList.layoutManager = linearLayoutManager
            dataList.adapter = adapter
            with(adViewModel) {
                fetchNews().observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            adapter.submitList(it.data) {
                                activity?.let { aty ->
                                    it.data?.let { list ->
                                        adList.add(setAdLoader(activity = aty, dataList, list, 20))
                                        adList.add(setAdLoader(activity = aty, dataList, list, 30))
                                    }
                                }
                            }
                        }

                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setAdLoader(
        activity: Activity,
        recyclerView: RecyclerView,
        list: ArrayList<ViewHolderType>,
        position: Int,
    ): AdLoader {
        val v = createView()
        return AdLoader()
            .init(activity, recyclerView, position)
            .forAd(object : OnAdLoadedListener {
                override fun onAdInitCompleted(adData: AdData) {
                    //create custom view in holder

                    val info = v.findViewById<TextView>(R.id.text_info)
                    info.text = adData.adName
                    info.setBackgroundColor(Color.RED)
                    list.add(
                        position,
                        ViewHolderType.Ad(v, adData)
                    )
                    adapter.submitList(list)
                }

            })
            .withAdListener(object : OnAdListener {
                override fun onAdImpression() {
                    //client handle impression event
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adList.forEach {
            it.release()
        }
    }

    private fun createView(): View {
        val binding = CreateItemBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.create_item, null)
        )
        return binding.root
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}