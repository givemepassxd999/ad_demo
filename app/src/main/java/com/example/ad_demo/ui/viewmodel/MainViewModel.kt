package com.example.ad_demo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ad_demo.repo.AdRepository
import com.example.ad_demo.utils.Resource
import com.example.ad_demo.utils.ViewHolderType
import kotlinx.coroutines.Dispatchers


class MainViewModel(
    private val adRepository: AdRepository
) : ViewModel() {
    private val list = arrayListOf<ViewHolderType>()
    fun fetchNews() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val titleList = adRepository.fetchNews().articles
            titleList?.forEach {
                list.add(ViewHolderType.Content(it.title ?: ""))
            }
            emit(Resource.success(data = list))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
}