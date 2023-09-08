package com.example.ad_demo.ui.view

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ad_demo.databinding.FragmentMainBinding
import com.example.ad_demo.ui.adapter.ItemAdapter
import com.example.ad_demo.ui.viewmodel.MainViewModel
import com.example.ad_demo.utils.ViewHolderType
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val adViewModel by viewModel<MainViewModel>()

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
        val list = arrayListOf<ViewHolderType>()
        for (i in 0..100) {
            if (i == 20) {
                list.add(ViewHolderType.Ad("www.google.com"))
            } else {
                list.add(ViewHolderType.Content(i.toString()))
            }
        }
        adapter.submitList(list)
        binding.dataList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                activity?.let { aty ->
                    val v = linearLayoutManager.findViewByPosition(20)
                    val rect = intArrayOf(0, 0)
                    v?.getLocationOnScreen(rect)
                    Log.d(
                        "@@",
                        v?.measuredHeight.toString() + " view y:${rect[1]} height:${
                            getScreenHeight(aty)
                        }"
                    )
                }
            }
        })
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