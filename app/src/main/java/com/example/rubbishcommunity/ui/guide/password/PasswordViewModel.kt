package com.example.rubbishcommunity.ui.guide.password

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.password.ResetPasswordRequestModel
import com.example.rubbishcommunity.ui.utils.*
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class PasswordViewModel(application: Application) : BaseViewModel(application) {
	
	
	//当前步骤
	val currentStep = MutableLiveData<Int>(STEP_INPUT_EMAIL)
	//账号
	val userAccount = MutableLiveData<String>("")
	//密码
	val password = MutableLiveData<String>("")
	//再次验证的密码
	val rePassword = MutableLiveData<String>("")
	//邮箱验证码
	val verifyCode = MutableLiveData<String>("")
	//是否正在Loading
	val isLoading = MutableLiveData<Boolean>(false)
	
	private val apiService by instance<UserService>()
	
	
	//发送邮箱，获取验证码
	fun sendEmail(): Single<ResultModel<String>>? {
		if (isEmail(userAccount.value ?: "")) {
			return apiService.sendEmail(userAccount.value!!)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(dealErrorCode())
				.compose(catchApiError())
				.doOnSuccess {
					//发送成功，将当前步骤设置为第二步输入验证码
					currentStep.value = STEP_INPUT_CODE
				}
		} else {
			sendError(
				ErrorData(
					ErrorType.UI_ERROR,
					"请输入正确的邮箱"
				)
			)
			return null
		}
	}
	
	//验证验证码并跳至最后一步
	fun judgeCodeAndGoStep3() {
		if (((verifyCode.value ?: "").length) == 6) {
			//将当前步骤设置为第三步输入新密码
			currentStep.value = STEP_INPUT_PASSWORD
		} else {
			sendError(
				ErrorData(
					ErrorType.UI_ERROR,
					"请完整输入验证码${(verifyCode.value ?: "").length}"
				)
			)
		}
	}
	
	//发送新密码至服务器
	fun editPassword(): Single<ResultModel<String>>? {
		if (password.value != null
			&& rePassword.value != null
			&& (password.value.equals(rePassword.value))
		) {
			
			if (judgePassword()) {
				//发送验证码
				return apiService.editPassword(
					ResetPasswordRequestModel(
						verifyCode.value!!,
						userAccount.value!!,
						password.value!!
					)
				)
					.switchThread()
					.compose(dealErrorCode())
					.compose(catchApiError())
					.compose(dealLoading())
			}
			return null
		} else {
			sendError(ErrorType.UI_ERROR, "两次密码不一致")
			return null
		}
	}
	
	
	private fun judgePassword(): Boolean {
		if (userAccount.value!!.isNotEmpty() && password.value!!.isNotEmpty()) {
			if (userAccount.value!!.length > 4) {
				if (password.value!!.length in 4..16) {
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
			obs.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
}