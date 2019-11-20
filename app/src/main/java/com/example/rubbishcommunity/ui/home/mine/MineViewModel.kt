package com.example.rubbishcommunity.ui.home.mine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class MineViewModel(application: Application) : BaseViewModel(application) {
	
	private val apiService by instance<UserService>()
	
	val userInfo = MutableLiveData<UsrProfile>()
	val isRefreshing = MutableLiveData<Boolean>()
	
	
	fun refreshUserInfo() {
		//获取用户详细信息
		val usrProfile = getLocalUserInfo()
		userInfo.postValue(usrProfile)
		
		//获取用户详细信息
		apiService.getUserProfile()
			.subscribeOn(Schedulers.io())
			.compose(dealErrorCode())
			.compose(dealError())
			.doOnSuccess {
				userInfo.postValue(it.data.usrProfile)
			}.compose(dealRefreshing())
			.bindLife()
	}
	
	
	fun logout(): Single<ResultModel<String>> {
		return apiService.logout()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealError())
	}
	
	private fun <T> dealRefreshing(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
}
