package com.example.rubbishcommunity.ui.home.find.moment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.model.api.moments.GetMomentsByClassifyRequestModel
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.model.api.moments.PageParam
import io.reactivex.Single
import org.kodein.di.generic.instance

class MomentsViewModel(application: Application) : BaseViewModel(application) {
	private val momentService by instance<MomentService>()
	val momentList = MutableLiveData<List<MomentContent>>()
	private val startPage = MutableLiveData(1)
	val isRefreshing = MutableLiveData(false)
	val isLoadingMore = MutableLiveData(false)
	val isLastPage = MutableLiveData(false)
	val classify = MutableLiveData(CLASSIFY_DYNAMIC)
	
	fun refreshMoments() {
		momentService.fetchMomentsByClassify(
			GetMomentsByClassifyRequestModel(classify.value!!, PageParam(1, 10))
		).dealRefresh()
			.doOnApiSuccess {
				isLastPage.postValue(it.data.pageInfoResp.lastPage)
				momentList.postValue(it.data.momentContentList)
				startPage.postValue(2)
			}
	}
	
	fun loadMoreMoments() {
		momentService.fetchMomentsByClassify(
			GetMomentsByClassifyRequestModel(
				classify.value!!,
				PageParam(startPage.value!!, 10)
			)
		).dealLoading()
			.doOnApiSuccess {
				isLastPage.postValue(it.data.pageInfoResp.lastPage)
				if (it.data.momentContentList.isNotEmpty())
					momentList.postValue(
						momentList.value
							?.toMutableList()
							?.apply {
								addAll(it.data.momentContentList)
							})
				startPage.postValue(startPage.value!! + 1)
			}
	}
	
	
	private fun <T> Single<T>.dealRefresh() =
		doOnSubscribe { isRefreshing.postValue(true) }
			.doFinally { isRefreshing.postValue(false) }
		
	
	private fun <T> Single<T>.dealLoading() =
		doOnSubscribe { isLoadingMore.postValue(true) }
			.doFinally { isLoadingMore.postValue(false) }
	
}