package com.example.rubbishcommunity.ui.home.find.questiontest

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.model.TestCard
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Single
import org.kodein.di.generic.instance


class QuestionTestViewModel(application: Application) : BaseViewModel(application) {
	
	private val rubbishService by instance<RubbishService>()
	val refreshing = MutableLiveData<Boolean>()
	val pagerList = MutableLiveData<List<TestCard>>()
	
	fun fetchTestList() {
		rubbishService.fetchQuestions()
			.dealRefresh()
			.doOnApiSuccess {
				pagerList.postValue(it.data)
			}
	}
	
	private fun <T> Single<T>.dealRefresh() =
		doOnSubscribe { refreshing.postValue(true) }
			.doFinally { refreshing.postValue(false) }
}