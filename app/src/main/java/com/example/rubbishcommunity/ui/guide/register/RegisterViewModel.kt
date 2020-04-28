package com.example.rubbishcommunity.ui.guide.register


import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.persistence.saveLinkKey
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.persistence.saveUserInfo
import com.example.rubbishcommunity.persistence.saveVerifyInfo
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.ui.utils.*
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import org.kodein.di.generic.instance
import java.util.*
import java.util.concurrent.TimeUnit


class RegisterViewModel(application: Application) : BaseViewModel(application) {
	
	val email = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	
	val isRegistering = MutableLiveData<Boolean>()
	
	
	private val apiService by instance<UserService>()
	
	fun init() {
		//初始化控件上的值
		email.value = ""
		password.value = ""
	}
	
	
	@SuppressLint("NewApi")
	fun registerAndLogin(): Single<ResultModel<LoginOrRegisterResultModel>>? {
		val versionName =
			getVersionName(MyApplication.instance)
		val deviceBrand = deviceBrand
		val osVersion = systemVersion
		val systemModel = systemModel
		
		if (judgeRegisterPrams(email.value!!, password.value!!)) {
			return apiService.loginOrRegister(
				LoginOrRegisterRequestModel(
					LoginOrRegisterRequestModel.DeviceInfo(
						versionName ?: "",
						deviceBrand,
						getPhoneIMEI(MyApplication.instance),
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
								versionName ?: "",
								deviceBrand,
								getPhoneIMEI(MyApplication.instance),
								"Android",
								systemModel,
								osVersion
							), 0,
							password.value ?: "",
							false,
							email.value ?: ""
						)
					)
				}
				
				.switchThread()
				.catchApiError()
				.dealRegistering()
				.doOnSuccess {
					//持久化得到的token以及用户登录的信息
					saveVerifyInfo(
						it.data.uin,
						email.value ?: "",
						password.value ?: "",
						it.data.token,
						it.data.openId,
						it.data.usrStatusFlag.emailVerifiedFlag,
						it.data.usrStatusFlag.needMoreInfoFlag
					)
					//存储用户个人信息
					saveUserInfo(
						it.data.usrProfile
					)
					//存储linkKey
					saveLinkKey()
					//登陆状态置为true
					saveLoginState(true)
				}
		}
		return null
	}
	
	
	private fun judgeRegisterPrams(userName: String, password: String): Boolean {
		return if (isEmail(userName)) {
			if (password.length in 4..16) {
				return true
			} else {
				sendError(
					ErrorData(
						ErrorType.UI_ERROR,
						"密码长度为6-16位"
					)
				)
			}
			false
		} else {
			sendError(
				ErrorData(
					ErrorType.UI_ERROR,
					"请输入正确的邮箱"
				)
			)
			false
		}
	}
	
	private fun <T> Single<T>.dealRegistering() = doOnSubscribe {
		isRegistering.postValue(true)
	}.doOnSuccess {
		isRegistering.postValue(false)
	}.doOnError {
		isRegistering.postValue(false)
	}
	
	
}