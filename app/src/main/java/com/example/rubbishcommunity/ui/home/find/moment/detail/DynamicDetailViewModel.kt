package com.example.rubbishcommunity.ui.home.find.moment.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.moments.MomentComment
import com.example.rubbishcommunity.model.api.moments.MomentContent
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class DynamicDetailViewModel(application: Application) : BaseViewModel(application) {
	
	private val momentService by instance<UserService>()
	val moment = MutableLiveData<MomentContent>()
	val isRefreshing = MutableLiveData<Boolean>()
	val inputComment = MutableLiveData<String>()
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs
				.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
	
	
}