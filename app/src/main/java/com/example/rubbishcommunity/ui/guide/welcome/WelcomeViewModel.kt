package com.example.rubbishcommunity.ui.guide.welcome

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.ui.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class WelcomeViewModel(application: Application) : BaseViewModel(application) {
	private val apiService by instance<ApiService>()
	val searchWord = MutableLiveData<String>()
	
	
	fun search(str: String): Single<ResultModel<String>> {
		return apiService.searchClassification(str)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealErrorCode())
			.compose(dealError())
	}
	
/*    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs
                .doOnSubscribe { isRefreshing.postValue(true) }
                .doOnSuccess { isRefreshing.postValue(false) }
                .doOnError { isRefreshing.postValue(false) }
        }
    }*/
	
	
}