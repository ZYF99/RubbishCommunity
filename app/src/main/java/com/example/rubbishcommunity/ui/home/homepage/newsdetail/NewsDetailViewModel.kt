package com.example.rubbishcommunity.ui.home.homepage.newsdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.ui.BaseViewModel


class NewsDetailViewModel(application: Application) : BaseViewModel(application) {
	val newsUrl = MutableLiveData<String>()
	
	
}