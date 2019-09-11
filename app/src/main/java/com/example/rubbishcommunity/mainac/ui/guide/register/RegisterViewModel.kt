package com.example.rubbishcommunity.mainac.ui.guide.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class RegisterViewModel(application: Application) : BaseViewModel(application) {
	
	
	private val apiService by instance<ApiService>()
	
	
	fun init() {
	
	
	}


/*    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs
                .doOnSubscribe { refreshing.postValue(true) }
                .doOnSuccess { refreshing.postValue(false) }
                .doOnError { refreshing.postValue(false) }
        }
    }*/
	
	
}