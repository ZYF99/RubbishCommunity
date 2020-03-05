package com.example.rubbishcommunity.ui.home.find.moment.detail.innercomment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class InnerCommentViewModel(application: Application) : BaseViewModel(application) {
	
	private val dynamicService by instance<UserService>()
	
	//评论列表
	val innerCommentList = MutableLiveData<MutableList<Comment>>()
	
	//我输入的评论
	val inputComment = MutableLiveData<String>()
	
	val isRefreshing = MutableLiveData<Boolean>()
	
	fun init(newInnerCommentList: MutableList<Comment>) {
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