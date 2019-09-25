package com.example.rubbishcommunity.ui.guide.register

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.persistence.saveUserInfo
import com.example.rubbishcommunity.persistence.saveVerifyInfo
import com.example.rubbishcommunity.utils.*
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class RegisterViewModel(application: Application) : BaseViewModel(application) {
	
	val userName = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	val rePassword = MutableLiveData<String>()
	
	//是否正在登陆
	val isLoading = MutableLiveData<Boolean>()
	//错误提示信息
	val errorMsg = MutableLiveData<String>()
	
	private val apiService by instance<ApiService>()
	
	
	fun init() {
		//初始化控件上的值
		userName.value = ""
		password.value = ""
		rePassword.value = ""
	}
	
	@SuppressLint("NewApi")
	fun registerOrLogin(): Single<ResultModel<LoginOrRegisterResultModel>>? {
		
		val versionName = AppUtils.getVersionName(MyApplication.instance)
		val deviceBrand = PhoneUtils.deviceBrand
		val osVersion = PhoneUtils.systemVersion
		val systemModel = PhoneUtils.systemModel
		if (judgeRegisterPrams(
				userName.value!!,
				password.value!!,
				rePassword.value!!
			)
		) {
			return apiService.loginOrRegister(
				LoginOrRegisterRequestModel(
					LoginOrRegisterRequestModel.DeviceInfo(
						versionName,
						deviceBrand,
						PhoneUtils.getPhoneIMEI(MyApplication.instance),
						osVersion,
						systemModel
					), 0,
					password.value!!,
					true,
					userName.value!!
				)
			).delay(1, TimeUnit.SECONDS)
				.compose(dealErrorCode())
				.compose(dealError())
				.flatMap {
					//登陆
					return@flatMap apiService.loginOrRegister(
						LoginOrRegisterRequestModel(
							LoginOrRegisterRequestModel.DeviceInfo(
								versionName,
								deviceBrand,
								PhoneUtils.getPhoneIMEI(MyApplication.instance),
								osVersion,
								systemModel
							), 0,
							password.value!!,
							false,
							userName.value!!
						)
					)
				}
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
	
	
	private fun judgeRegisterPrams(
		userName: String,
		password: String,
		rePassword: String
	): Boolean {
		if (userName.length > 4) {
			if (password.length in 4..16) {
				if (password != rePassword) {
					sendError(ErrorData(ErrorType.INPUT_ERROR, "两次密码不一致"))
				} else {
					return true
				}
			} else {
				sendError(ErrorData(ErrorType.INPUT_ERROR, "密码长度为6-16位"))
			}
		} else {
			sendError(ErrorData(ErrorType.INPUT_ERROR, "用户名必须大于4位"))
		}
		return false
	}
	
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe {
				
				isLoading.postValue(true)
			}
				.doOnSuccess {
					
					//isLoading.postValue(false)
				}
				.doOnError {
					
					isLoading.postValue(false)
				}
		}
	}
	
	
}