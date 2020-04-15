package com.example.rubbishcommunity.ui.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.model.api.search.Category
import com.example.rubbishcommunity.persistence.*
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Observable
import org.kodein.di.generic.instance

class SplashViewModel(application: Application) : BaseViewModel(application) {
	
	val imgUrl =
		MutableLiveData("http://222.186.12.239:10010/chuangyiwen_20190402/005.jpg")
	
	private val rubbishService by instance<RubbishService>()
	
	fun fetchClassificationInfo(action: () -> Unit) {
		val categoryList = mutableListOf<Category>()
			Observable.fromIterable(listOf(1, 2, 3, 4))
				.flatMapSingle { num ->
					rubbishService.searchCategoryByName(num)
				}.map {
					categoryList.add(it.data)
					categoryList
				}.doOnComplete {
					saveClassificationMap(categoryList)
				}.switchThread()
				.catchApiError()
				.doOnComplete {
					action.invoke()
				}
				.bindLife()
	}
}
