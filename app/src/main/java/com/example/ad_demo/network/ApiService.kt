package com.example.ad_demo.network

import com.example.ad_demo.data.response.NewsResponse
import retrofit2.http.GET

interface ApiService {
    @GET("everything?q=apple&from=2023-09-08&to=2023-09-08&sortBy=popularity&apiKey=737bb2d5069940bfab237e1559e1ff1f")
    suspend fun getNews(): NewsResponse
}