package com.example.ad_sdk

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import androidx.recyclerview.widget.RecyclerView

object AdSdk {
    fun init(activity: Activity, recyclerView: RecyclerView, position: Int) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adView = recyclerView.layoutManager?.findViewByPosition(position)
                val rect = intArrayOf(0, 0)
                adView?.getLocationOnScreen(rect)
                Log.d(
                    "@@",
                    adView?.measuredHeight.toString() + " view y:${rect[1]} height:${
                        getScreenHeight(activity)
                    }"
                )
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
}