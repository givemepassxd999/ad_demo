package com.example.ad_demo.repo

import com.example.ad_demo.data.response.NewsResponse

abstract class AdRepository : Repository {
    abstract suspend fun fetchNews(): NewsResponse
}