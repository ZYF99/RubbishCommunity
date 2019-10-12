package com.example.rubbishcommunity.ui.guide.basicinfo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.CompleteInfoRequestModel
import com.example.rubbishcommunity.persistence.getLocalEmail
import com.example.rubbishcommunity.ui.utils.ErrorData
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance

class BasicInfoViewModel(application: Application) : BaseViewModel(application) {
	
	val verifyCode = MutableLiveData<String>()
	val avatar = MutableLiveData<String>()
	val gender = MutableLiveData<String>()
	val location = MutableLiveData<BDLocation>()
	val name = MutableLiveData<String>()
	val birthInt = MutableLiveData<Long>()
	val birthString = MutableLiveData<String>()

	val isLoading = MutableLiveData<Boolean>()
	private val apiService by instance<ApiService>()
	
	//上传头像
	fun upLoadAvatar(path:String){
		upLoadImage(apiService,path,object :QiNiuUtil.QiNiuUpLoadListener{
			override fun onSuccess(s: String) {
				avatar.postValue(path)
			}
			
			override fun onProgress(percent: Int) {
			
			}
			
		}).bindLife()
		
	}
	
	//发送邮箱验证码
	fun sendEmail(): Single<ResultModel<String>> {
		return apiService.sendEmail(getLocalEmail())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealErrorCode())
			.compose(dealError())
	}
	
	//完善用户信息
	fun completeInfo(): Single<ResultModel<String>>? {
		if (judgeCompleteInfoParams()) {
			return apiService.completeInfo(
				CompleteInfoRequestModel(
					avatar.value ?: "",
					birthInt.value ?: 0,
					verifyCode.value ?: "",
					gender.value ?: "男",
					CompleteInfoRequestModel.LocationReq(
						location.value?.city ?: "",
						location.value?.country ?: "",
						location.value?.district ?: "",
						location.value?.latitude ?: 0.0,
						location.value?.longitude ?: 0.0,
						location.value?.province ?: "",
						location.value?.street ?: ""
					),
					name.value ?: ""
				)
			).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(dealErrorCode())
				.compose(dealError())
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
		if ((verifyCode.value ?: "").length == 6) {
			if ((name.value ?: "").length in 4..8) {
				if ((birthString.value ?: "").length > 4) {
					return true
				} else {
					sendError(
						ErrorData(
							ErrorType.INPUT_ERROR,
							"请选择出生日期"
						)
					)
				}
			} else {
				sendError(
					ErrorData(
						ErrorType.INPUT_ERROR,
						"昵称长度必须在6-16位之间"
					)
				)
			}
		} else {
			sendError(
				ErrorData(
					ErrorType.INPUT_ERROR,
					"请输入完整的验证码"
				)
			)
		}
		return false
	}
	
	
}