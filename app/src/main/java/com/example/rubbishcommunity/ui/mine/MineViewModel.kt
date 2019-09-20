package com.example.rubbishcommunity.ui.mine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.mine.UserInfoResultModel
import com.example.rubbishcommunity.persistence.getLocalUserName
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class MineViewModel(application: Application) : BaseViewModel(application) {
	
	private val apiService by instance<ApiService>()
	
	val userInfo = MutableLiveData<UserInfoResultModel>()
	val isRefreshing = MutableLiveData<Boolean>()
	
	
	fun getUserInfo() {
		return apiService
			.getUserInfo(getLocalUserName())
			.compose(dealErrorCode())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealRefreshing())
			.compose(dealError())
			.doOnSuccess {
				userInfo.postValue(it.data)
			}
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
			obs.doOnSubscribe {
				isRefreshing.postValue(true)
			}
				.doOnSuccess {
					isRefreshing.postValue(false)
				}
				.doOnError {
					isRefreshing.postValue(false)
				}
		}
	}
	
	
	
}