package com.example.rubbishcommunity.ui.home.homepage.newsdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.model.api.News
import com.example.rubbishcommunity.model.api.news.NewsResult
import com.example.rubbishcommunity.ui.base.BaseViewModel


class NewsDetailViewModel(application: Application) : BaseViewModel(application) {
	val news = MutableLiveData<NewsResult.News>()
}