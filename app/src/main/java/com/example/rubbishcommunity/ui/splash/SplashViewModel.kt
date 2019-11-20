package com.example.rubbishcommunity.ui.splash

import android.app.Application
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Observable
import org.kodein.di.generic.instance


class SplashViewModel(application: Application) : BaseViewModel(application) {
	
	private val rubbishService by instance<RubbishService>()
	
	fun initClassification() {
		Observable.fromIterable(listOf(1, 2, 3, 4)).flatMapSingle { num ->
			rubbishService.searchCategoryByName(num)
		}.doOnNext { category ->
			MyApplication.instance.classificationMap[category.data.id] = category.data
		}.bindLife()
	}
	
}