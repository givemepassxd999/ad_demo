package com.example.ad_demo.utils

sealed class ViewHolderType(val type: Int) {

    class Content(var content: String) : ViewHolderType(CONTENT)
    class Ad(var url: String) : ViewHolderType(AD)

    companion object {
        const val CONTENT = 0
        const val AD = 1
    }
}