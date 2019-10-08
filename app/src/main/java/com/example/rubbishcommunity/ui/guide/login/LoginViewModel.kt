package com.example.rubbishcommunity.ui.guide.login


import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.persistence.*
import com.example.rubbishcommunity.utils.*
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class LoginViewModel(application: Application) : BaseViewModel(application) {
	
	//账号
	val userName = MutableLiveData<String>()
	//密码
	val password = MutableLiveData<String>()
	//是否正在登陆
	val isLoading = MutableLiveData<Boolean>()
	
	private val apiService by instance<ApiService>()
	
	fun init() {
		//必须初始化控件上的值
		userName.value = getLocalUserName()
		password.value = getLocalPassword()
	}
	
	@RequiresApi(Build.VERSION_CODES.O)
	fun login(): Single<ResultModel<LoginOrRegisterResultModel>>? {
		val versionName = AppUtils.getVersionName(MyApplication.instance)
		val deviceBrand = PhoneUtils.deviceBrand
		val osVersion = PhoneUtils.systemVersion
		val systemModel = PhoneUtils.systemModel
		
		if (judgeLoginParams()) {
			return apiService.loginOrRegister(
				LoginOrRegisterRequestModel(
					LoginOrRegisterRequestModel.DeviceInfo(
						versionName,
						deviceBrand,
						PhoneUtils.getPhoneIMEI(MyApplication.instance),
						"Android",
						systemModel,
						osVersion
						
					), 0,
					password.value!!,
					false,
					userName.value!!
				)
			).delay(1, TimeUnit.SECONDS)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(dealLoading())
				.compose(dealErrorCode())
				.compose(dealError())
				.doOnSuccess {
					//持久化得到的token以及用户登录的信息
					saveVerifyInfo(
						userName.value!!,
						password.value!!,
						it.data.token
					)
					//存储用户个人信息
					saveUserInfo(
						it.data.usrProfile
					)
					//登陆状态置为true
					saveLoginState(true)
				}
		}
		return null
	}
	
	private fun judgeLoginParams(): Boolean {
		if (userName.value!!.isNotEmpty() && password.value!!.isNotEmpty()) {
			if (userName.value!!.length > 4) {
				if (password.value!!.length in 4..16) {
					return true
				} else {
					sendError(ErrorData(ErrorType.INPUT_ERROR, "密码长度为6-16位"))
				}
			} else {
				sendError(ErrorData(ErrorType.INPUT_ERROR, "用户名必须大于4位"))
			}
		} else {
			sendError(ErrorData(ErrorType.INPUT_ERROR, "用户名和密码不能为空"))
		}
		return false
	}
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
}