package com.example.rubbishcommunity.ui.release.dynamic


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.ServerBackException
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicRequestModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicResultModel
import com.example.rubbishcommunity.ui.guide.ServerCallBackCode
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance



class ReleaseDynamicViewModel(application: Application) : BaseViewModel(application) {
	
	val isLoading = MutableLiveData<Boolean>()
	
	val title = MutableLiveData<String>()
	
	private val apiService by instance<ApiService>()
	
	fun init() {
		//必须初始化控件上的值
		title.postValue("发布动态")
	}
	
	
	fun release(): Single<ResultModel<ReleaseDynamicResultModel>>? {
		return apiService.releaseDynamic(
			ReleaseDynamicRequestModel(
				"!!!!我要发布动态!!!!"
			)
		).subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealLoading())
			.compose(dealErrorCode())
			.compose(dealError())
	}
	
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
}