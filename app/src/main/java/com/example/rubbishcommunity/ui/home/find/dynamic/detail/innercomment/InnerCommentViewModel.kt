package com.example.rubbishcommunity.ui.home.find.dynamic.detail.innercomment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.BaseViewModel
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class InnerCommentViewModel(application: Application) : BaseViewModel(application) {
	
	private val dynamicService by instance<ApiService>()
	
	
	//评论列表
	val innerCommentList = MutableLiveData<List<Comment>>()
	
	val isRefreshing = MutableLiveData<Boolean>()
	
	fun init(newInnerCommentList: List<Comment>) {
		innerCommentList.value = newInnerCommentList
	}
	
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs
				.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
	
	
}