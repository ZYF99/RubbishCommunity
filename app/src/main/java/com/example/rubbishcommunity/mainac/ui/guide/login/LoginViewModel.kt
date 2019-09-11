package com.example.rubbishcommunity.mainac.ui.guide.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.base.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.login.LoginRequestModel
import com.example.rubbishcommunity.model.api.login.LoginResultModel
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class LoginViewModel(application: Application) : BaseViewModel(application) {
	
	val userName = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	
	private val apiService by instance<ApiService>()
	
	fun init() {
		userName.value = (SharedPreferencesUtils.getData("userName","") as String)
		password.value = (SharedPreferencesUtils.getData("password","") as String)
	}
	
	fun login(loginRequestModel: LoginRequestModel): Single<ResultModel<LoginResultModel>> {
		return apiService.login(
			loginRequestModel
		).subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealError())
	}
	
	fun saveVerifyInfo(token:String){
		SharedPreferencesUtils.putData("userName",userName.value!!)
		SharedPreferencesUtils.putData("password",password.value!!)
		SharedPreferencesUtils.putData("token",token)
	}
	
	
}