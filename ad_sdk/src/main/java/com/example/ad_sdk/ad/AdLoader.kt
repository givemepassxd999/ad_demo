package com.example.ad_sdk.ad

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AdLoader {
    private var onAdListener: OnAdListener? = null
    private var activity: Activity? = null
    private var flag = false
    private var isTimerStart = false

    fun forAd(listener: OnAdLoadedListener): AdLoader {
        val adData = AdData(1, "test", "https://i.ytimg.com/vi/SMUas3cP2Q4/mqdefault.jpg")
        listener.onAdInitCompleted(adData)
        return this
    }

    fun withAdListener(listener: OnAdListener): AdLoader {
        onAdListener = listener
        return this
    }

    fun init(activity: Activity, recyclerView: RecyclerView, number: Int): AdLoader {
        this.activity = activity
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var isTimerStart = false
            private var countDownTimer = createTimer(number)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adView = recyclerView.layoutManager?.findViewByPosition(number)
                val rect = intArrayOf(0, 0)
                adView?.getLocationOnScreen(rect)
                adView?.let {
                    if ((getScreenHeight(activity) - rect[1]) > (adView.measuredHeight / 2)) {
                        if (isTimerStart.not().and(flag.not())) {
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
        })
        return this
    }

    private fun createTimer(number: Int): CountDownTimer {
        val countDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //nothing to do
            }

            override fun onFinish() {
                Toast.makeText(
                    activity,
                    "The No. $number AD is get reward",
                    Toast.LENGTH_SHORT
                )
                    .show()
                onAdListener?.onAdImpression()
                isTimerStart = false
                flag = true
            }
        }
        return countDownTimer
    }

    fun release() {
        onAdListener = null
        activity = null
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
