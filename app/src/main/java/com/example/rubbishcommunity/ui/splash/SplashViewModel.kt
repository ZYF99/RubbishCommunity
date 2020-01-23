package com.example.rubbishcommunity.ui.splash

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.model.api.search.Category
import com.example.rubbishcommunity.persistence.*
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Observable
import org.kodein.di.generic.instance


class SplashViewModel(application: Application) : BaseViewModel(application) {
	
	val imgUrl =
		MutableLiveData("http://img.zcool.cn/community/01f96455547f4e0000009af03b9eb8.jpg@2o.jpg")
	
	private val rubbishService by instance<RubbishService>()
	
	
	@SuppressLint("UseSparseArrays")
	fun initClassification(): Observable<MutableList<Category>> {
		val categoryList = mutableListOf<Category>()
		return Observable.fromIterable(listOf(1, 2, 3, 4))
			.flatMapSingle { num ->
				rubbishService.searchCategoryByName(num)
			}.map {
				categoryList.add(it.data)
				categoryList
			}.doOnComplete {
				saveClassificationMap(categoryList)
			}
	}
	
}
