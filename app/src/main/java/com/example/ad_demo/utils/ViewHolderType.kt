package com.example.ad_demo.utils

import android.view.View

sealed class ViewHolderType(val type: Int) {

    class Content(var content: String) : ViewHolderType(CONTENT)
    class Ad(var adUrl: String) : ViewHolderType(AD)

    companion object {
        const val CONTENT = 0
        const val AD = 1
    }
}