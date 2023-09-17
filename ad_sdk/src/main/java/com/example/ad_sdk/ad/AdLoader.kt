package com.example.ad_sdk.ad

import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

object AdLoader {
    private var onAdListener: OnAdListener? = null
    private var activity: FragmentActivity? = null
    fun forAd(listener: OnAdLoadedListener): AdLoader {
        val adData = AdData(1, "adName", "https://i.ytimg.com/vi/SMUas3cP2Q4/mqdefault.jpg")
        listener.onAdInitCompleted(adData)
        return this
    }

    fun withAdListener(listener: OnAdListener): AdLoader {
        onAdListener = listener
        return this
    }

    fun init(activity: FragmentActivity, recyclerView: RecyclerView, position: Int): AdLoader {
        this.activity = activity
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var isTimerStart = false
            private var countDownTimer = createTimer()
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adView = recyclerView.layoutManager?.findViewByPosition(position)
                val rect = intArrayOf(0, 0)
                adView?.getLocationOnScreen(rect)
                adView?.let {
                    if ((getScreenHeight(activity) - rect[1]) > (adView.measuredHeight / 2)) {
                        if (isTimerStart.not()) {
                            Log.d("@@", "count timer start")
                            isTimerStart = true
                            countDownTimer.start()
                        } else {
                            //noting to do
                        }
                    } else {
                        Log.d("@@", "count timer stop")
                        isTimerStart = false
                        countDownTimer.cancel()
                    }
                }
            }

            private fun createTimer(): CountDownTimer {
                val countDownTimer = object : CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.d("@@", "count timer onTick ${millisUntilFinished / 1000}")
                    }

                    override fun onFinish() {
                        Log.d("@@", "impression")
                        onAdListener?.onAdImpression()
                        isTimerStart = false
                    }
                }
                return countDownTimer
            }
        })
        return this
    }

    private fun saveCount(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
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
