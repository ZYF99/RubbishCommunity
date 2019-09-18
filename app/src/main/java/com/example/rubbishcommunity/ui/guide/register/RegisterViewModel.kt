package com.example.rubbishcommunity.ui.guide.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class RegisterViewModel(application: Application) : BaseViewModel(application) {
	val userName = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	val rePassword = MutableLiveData<String>()
	
	private val apiService by instance<ApiService>()
	
	
	fun init() {
		//必须初始化控件上的值
		userName.value = ""
		password.value = ""
		rePassword.value = ""
	}
	
	fun registerOrLogin(loginOrRegisterRequestModel: LoginOrRegisterRequestModel): Single<ResultModel<LoginOrRegisterResultModel>> {
		return apiService.loginOrRegister(
			loginOrRegisterRequestModel
		).delay(2, TimeUnit.SECONDS)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealError())
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