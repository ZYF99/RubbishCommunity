package com.example.rubbishcommunity.ui.home.find.vote

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class VoteViewModel(application: Application) : BaseViewModel(application) {
	
	
	private val dynamicService by instance<MomentService>()
	
	val voteList = MutableLiveData<MutableList<Vote>>()
	
	val refreshing = MutableLiveData<Boolean>()
	
	fun init() {
		voteList.value = mutableListOf()
		getVoteList()
		
	}
	
	fun getVoteList() {
		mockData()
		dynamicService.getVoteList(0)
			.dealRefresh()
			.doOnApiSuccess {
				voteList.value = it
			}
	}
	
	private fun mockData() {
		val item = Vote("Take part in green action to protect beautiful home.")
		
		voteList.value = mutableListOf(item, item, item, item, item, item)
	}
	
	
	private fun <T> Single<T>.dealRefresh() =
		doOnSubscribe { refreshing.postValue(true) }
			.doFinally { refreshing.postValue(false) }
	
	
	
}