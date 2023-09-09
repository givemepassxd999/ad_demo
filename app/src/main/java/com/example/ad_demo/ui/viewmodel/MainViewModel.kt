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
    fun fetchNews() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val titleList = adRepository.fetchNews().articles
            val list = arrayListOf<ViewHolderType>()
            titleList?.forEachIndexed { index, s ->
                if (index == 20) {
                    list.add(ViewHolderType.Ad("https://i.ytimg.com/vi/SMUas3cP2Q4/mqdefault.jpg"))
                } else {
                    list.add(ViewHolderType.Content(s.title ?: ""))
                }
            }
            emit(Resource.success(data = list))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
}