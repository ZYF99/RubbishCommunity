package com.example.rubbishcommunity.ui.guide.register


import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.persistence.saveUserInfo
import com.example.rubbishcommunity.persistence.saveVerifyInfo
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.utils.*
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class RegisterViewModel(application: Application) : BaseViewModel(application) {
	
	val email = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	
	val isLoading = MutableLiveData<Boolean>()
	
	
	private val apiService by instance<ApiService>()
	
	fun init() {
		//初始化控件上的值
		email.value = ""
		password.value = ""
	}
	
	
	@SuppressLint("NewApi")
	fun registerAndLogin(): Single<ResultModel<LoginOrRegisterResultModel>>? {
		val versionName = AppUtils.getVersionName(MyApplication.instance)
		val deviceBrand = PhoneUtils.deviceBrand
		val osVersion = PhoneUtils.systemVersion
		val systemModel = PhoneUtils.systemModel
		
		if (judgeRegisterPrams(email.value!!,password.value!!)) {
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
					true,
					email.value!!
				)
			).delay(1, TimeUnit.SECONDS)
				.compose(dealErrorCode())
				.flatMap {
					//登陆
					return@flatMap apiService.loginOrRegister(
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
							email.value!!
						)
					)
				}.compose(dealErrorCode())
				.compose(dealError())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				
				.doOnSuccess {
					//持久化得到的token以及用户登录的信息
					saveVerifyInfo(
						email.value!!,
						password.value!!,
						it.data.token,
						it.data.openId,
						it.data.usrStatusFlag.emailVerifiedFlag,
						it.data.usrStatusFlag.needMoreInfoFlag
					)
					//存储用户个人信息
					saveUserInfo(
						it.data.usrProfile
					)
					//登陆状态置为true
					saveLoginState(true)
				}.compose(dealLoading())
		}
		return null
	}
	
	
	
	private fun judgeRegisterPrams(userName: String, password: String): Boolean {
		return if (isEmail(userName)) {
			if (password.length in 4..16) {
				return true
			} else {
				sendError(ErrorData(ErrorType.INPUT_ERROR, "密码长度为6-16位"))
			}
			false
		} else {
			sendError(ErrorData(ErrorType.INPUT_ERROR, "请输入正确的邮箱"))
			false
		}
	}
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe {
				isLoading.postValue(true)
			}
				.doOnSuccess {
					isLoading.postValue(false)
				}
				.doOnError {
					isLoading.postValue(false)
				}
		}
	}
	
	
}