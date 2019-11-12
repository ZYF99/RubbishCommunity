package com.example.rubbishcommunity.ui.splash

import android.app.Application
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.ui.BaseViewModel
import io.reactivex.Observable
import io.reactivex.Single
import org.kodein.di.generic.instance
import java.util.*


class SplashViewModel(application: Application) : BaseViewModel(application) {
	
	private val apiService by instance<ApiService>()
	
	fun initClassification() {
		Observable.fromIterable(listOf(1, 2, 3, 4)).flatMapSingle { num ->
			apiService.searchCategoryByName(num)
		}.doOnNext { category ->
			MyApplication.instance.classificationMap[category.data.id] = category.data
		}.bindLife()
	}
	
}