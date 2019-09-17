package com.example.rubbishcommunity.ui.guide.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.base.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class LoginViewModel(application: Application) : BaseViewModel(application) {
	
	val userName = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	
	private val apiService by instance<ApiService>()
	
	fun init() {
		//必须初始化控件上的值
		userName.value = (SharedPreferencesUtils.getData("userName", "") as String)
		password.value = (SharedPreferencesUtils.getData("password", "") as String)
	}
	
	fun login(loginOrRegisterRequestModel: LoginOrRegisterRequestModel): Single<ResultModel<LoginOrRegisterResultModel>> {
		return apiService.loginOrRegister(
			loginOrRegisterRequestModel
		).delay(2, TimeUnit.SECONDS)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealError())
	}
	
	//存储用于验证的信息
	fun saveVerifyInfo(token: String) {
		SharedPreferencesUtils.putData("userName", userName.value!!)
		SharedPreferencesUtils.putData("password", password.value!!)
		SharedPreferencesUtils.putData("token", token)
	}

	
	
}