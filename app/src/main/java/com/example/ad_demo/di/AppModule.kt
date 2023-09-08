package com.example.ad_demo.di

import com.example.ad_demo.data.AdRepositoryImpl
import com.example.ad_demo.network.ApiService
import com.example.ad_demo.network.AppClientManager
import com.example.ad_demo.repo.AdRepository
import com.example.ad_demo.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single<ApiService> {
            AppClientManager.creteRestfulApiClient().create(ApiService::class.java)
        }
    }
    val vmModule = module {
        viewModel { MainViewModel(get()) }
    }
    val repoModule = module {
        single<AdRepository> { AdRepositoryImpl(get()) }
    }
}