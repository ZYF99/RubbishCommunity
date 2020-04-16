package com.example.rubbishcommunity.ui.guide.basicinfo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.CompleteInfoRequestModel
import com.example.rubbishcommunity.persistence.changeEmailVerifiedFlag
import com.example.rubbishcommunity.persistence.getLocalEmail
import com.example.rubbishcommunity.persistence.updateSomeUserInfo
import com.example.rubbishcommunity.ui.home.mine.editinfo.getImageUrlFromServer
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance
import java.util.*

class BasicInfoViewModel(application: Application) : BaseViewModel(application) {
	
	val verifyCode = MutableLiveData("")
	val avatar = MutableLiveData("")
	val gender = MutableLiveData("男")
	val city = MutableLiveData<String>()
	val name = MutableLiveData("")
	val birthday = MutableLiveData(23558400000) //1970-10-1 00:00:00
	val isLoading = MutableLiveData(false)
	private val apiService by instance<UserService>()
	private val imageService by instance<ImageService>()
	
	//上传头像
	fun upLoadAvatar(path: String) {
		imageService.upLoadImage(
			path
		) {}.doOnSuccess { key ->
			avatar.postValue(getImageUrlFromServer(key))
		}.bindLife()
	}
	
	//发送邮箱验证码
	fun sendEmail(): Single<ResultModel<String>> {
		return apiService.sendEmail(getLocalEmail())
			.switchThread()
			.compose(dealErrorCode())
			.compose(catchApiError())
	}
	
	//完善用户信息
	fun completeInfo(): Single<ResultModel<String>>? {
		if (judgeCompleteInfoParams()) {
			return apiService.completeInfo(
				CompleteInfoRequestModel(
					getLocalEmail(),
					avatar.value!!,
					birthday.value!!,
					(verifyCode.value!!).toUpperCase(Locale.ENGLISH),
					gender.value!!,
					0,
					CompleteInfoRequestModel.LocationReq(
						city.value?:"",
						"默认国家",
						"默认区县",
						0.0,
						 0.0,
						 "默认省份",
						"默认街道"
					),
					name.value!!
				)
			).switchThread()
				.doOnSuccess {
				//改变邮箱验证状态为已验证
				changeEmailVerifiedFlag(true)
				//更新本地基本信息
				updateSomeUserInfo(
					avatar.value!!,
					gender.value!!,
					name.value!!,
					birthday.value!!,
					city.value?:"默认城市"
				)
			}
				.catchApiError()
				.compose(dealLoading())
		} else {
			return null
		}
		
		
	}
	
	//刷新机制
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
	
	//判断填写的参数
	private fun judgeCompleteInfoParams(): Boolean {
		
		fun sendInputError(errorMsg: String) {
			sendError(
				ErrorType.UI_ERROR,
				errorMsg
			)
		}
		
		return when {
			verifyCode.value!!.length < 6 -> {
				sendInputError("请输入完整的验证码")
				false
			}
			name.value!!.length !in 3..10 -> {
				sendInputError("昵称长度必须大于2位")
				false
			}
			avatar.value!!.isEmpty() -> {
				sendInputError("请先上传头像")
				false
			}
			else -> true
		}
	}
	
	
}