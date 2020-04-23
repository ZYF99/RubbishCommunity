package com.example.rubbishcommunity.ui.guide.login


import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.persistence.*
import com.example.rubbishcommunity.ui.utils.*
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class LoginViewModel(application: Application) : BaseViewModel(application) {
	
	//账号
	val userName = MutableLiveData<String>()
	//密码
	val password = MutableLiveData<String>()
	//是否正在登陆
	val isLoging = MutableLiveData<Boolean>()
	
	private val userService by instance<UserService>()
	
	fun init() {
		//必须初始化控件上的值
		userName.value = getLocalEmail()
		password.value = getLocalPassword()
	}
	
	@RequiresApi(Build.VERSION_CODES.O)
	
	
	
	fun login(): Single<ResultModel<LoginOrRegisterResultModel>>? {
		val versionName = getVersionName(MyApplication.instance) //APP版本号
		val deviceBrand = deviceBrand //手机品牌
		val osVersion = systemVersion //系统版本号
		val systemModel = systemModel //系统
		if (judgeLoginParams()) {
			return userService.loginOrRegister(
				LoginOrRegisterRequestModel( //构造请求Body的Model
					LoginOrRegisterRequestModel.DeviceInfo(
						versionName?:"",
						deviceBrand,
						getPhoneIMEI(MyApplication.instance),
						"Android",
						systemModel,
						osVersion
						
					), 0,
					password.value?:"",
					false,
					userName.value?:""
				)
			).delay(1, TimeUnit.SECONDS)
				.switchThread()
				.catchApiError()
				.doOnSuccess {
					//持久化得到的token以及用户登录的信息
					saveVerifyInfo(
						it.data.uin,
						userName.value?:"",
						password.value?:"",
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
				}
				.catchApiError()
				.compose(dealLoading())
		}
		return null
	}
	
	private fun judgeLoginParams(): Boolean {
		if (userName.value!!.isNotEmpty() && password.value!!.isNotEmpty()) {
			if (userName.value!!.length > 4) {
				if (password.value!!.length in 6..16) {
					return true
				} else {
					sendError(
						ErrorData(
							ErrorType.UI_ERROR,
							"密码长度为6-16位"
						)
					)
				}
			} else {
				sendError(
					ErrorData(
						ErrorType.UI_ERROR,
						"用户名必须大于4位"
					)
				)
			}
		} else {
			sendError(
				ErrorData(
					ErrorType.UI_ERROR,
					"用户名和密码不能为空"
				)
			)
		}
		return false
	}
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isLoging.postValue(true) }
				.doOnSuccess { isLoging.postValue(false) }
				.doOnError { isLoging.postValue(false) }
		}
	}
	
}