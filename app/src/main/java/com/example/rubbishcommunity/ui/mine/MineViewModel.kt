package com.example.rubbishcommunity.ui.mine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.mine.UserInfoResultModel
import com.example.rubbishcommunity.persistence.getLocalUserName
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import timber.log.Timber

class MineViewModel(application: Application) : BaseViewModel(application) {
	
	private val apiService by instance<ApiService>()
	
	val userInfo = MutableLiveData<UserInfoResultModel>()
	
	
	
	fun getUserInfo() :Single<ResultModel<UserInfoResultModel>>{
		return apiService
			.getUserInfo(getLocalUserName())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealError())

	}
	
	fun logout():Single<ResultModel<String>>{
		return apiService.logout()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealError())
		
	}
	
	
}