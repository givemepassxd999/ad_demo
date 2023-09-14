package com.example.ad_sdk.ad

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class AdLoader private constructor() {
    companion object {
        class Builder(private val activity: FragmentActivity) {
            private var onAdListener: OnAdListener? = null
            fun forAd(listener: OnAdLoadedListener): Builder {
                val adData = AdData(1, "adName", "https://i.ytimg.com/vi/SMUas3cP2Q4/mqdefault.jpg")
                listener.onAdData(adData)
                return this
            }

            fun withAdListener(listener: OnAdListener): Builder {
                onAdListener = listener
                return this
            }

            fun build(): AdLoader {
                return AdLoader()
            }

            fun init(recyclerView: RecyclerView, position: Int): Builder {
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
                        val countDownTimer = object : CountDownTimer(10000, 1000) {
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
    }
}
