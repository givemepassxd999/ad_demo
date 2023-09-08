package com.example.ad_demo.data

import com.example.ad_demo.network.ApiService
import com.example.ad_demo.repo.AdRepository

class AdRepositoryImpl(private val apiService: ApiService) : AdRepository() {

}