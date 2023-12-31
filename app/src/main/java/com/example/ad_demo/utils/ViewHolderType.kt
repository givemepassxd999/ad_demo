package com.example.ad_demo.utils

import android.view.View
import com.example.ad_sdk.ad.AdData

sealed class ViewHolderType(val type: Int) {

    class Content(var content: String) : ViewHolderType(CONTENT)
    class Ad(var view: View? = null, var adData: AdData) : ViewHolderType(AD)

    companion object {
        const val CONTENT = 0
        const val AD = 1
    }
}