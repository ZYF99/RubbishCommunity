package com.example.rubbishcommunity.ui.release.dynamic


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.*
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance
import com.example.rubbishcommunity.utils.ErrorData
import com.example.rubbishcommunity.utils.ErrorType
import com.example.rubbishcommunity.utils.sendError


class ReleaseDynamicViewModel(application: Application) : BaseViewModel(application) {
	//Toolbar标题栏
	val toolbarTitle = MutableLiveData<String>()
	
	//是否正在加载
	val isLoading = MutableLiveData<Boolean>()
	
	//已选列表
	val selectedList = MutableLiveData<MutableList<LocalMedia>>()
	
	//标题
	val title = MutableLiveData<String>()
	
	//内容
	val content = MutableLiveData<String>()
	
	//位置
	val location = MutableLiveData<String>()
	
	//进度
	val progress = MutableLiveData<Int>()
	
	private val apiService by instance<ApiService>()
	
	fun init() {
		//必须初始化控件上的值
		toolbarTitle.value = ("发布动态")
		selectedList.value =
			when {
				SharedPreferencesUtils.getListData(
					"draft_selectedList",
					LocalMedia::class.java
				).isNullOrEmpty() -> mutableListOf()
				else -> SharedPreferencesUtils.getListData(
					"draft_selectedList",
					LocalMedia::class.java
				)
			}
		title.postValue(
			SharedPreferencesUtils.getData("draft_tittle", "") as String
		)
		content.postValue(
			SharedPreferencesUtils.getData("draft_content", "") as String
		)
		progress.postValue(0)
	}
	
	fun getLocation() {
		val locationClient = initLocationOption(MyApplication.instance)
		locationClient.registerLocationListener(object : BDAbstractLocationListener() {
			override fun onReceiveLocation(bdLocation: BDLocation?) {
				if (bdLocation != null) {
					location.postValue(
						"${bdLocation.locationDescribe}\n"
						/*			"经度 ${bdLocation.longitude}\n" +"纬度 ${bdLocation.latitude}\n" +"详细地址信息 ${bdLocation.addrStr}\n" +"国家 ${bdLocation.country}\n" +"省份 ${bdLocation.province}\n" +"城市 ${bdLocation.city}\n" +"区县 ${bdLocation.district}\n" +"街道 ${bdLocation.street}\n"*/
					)
				} else {
					MyApplication.showToast("null")
				}
				
			}
		})
		locationClient.start()
	}
	
	fun saveDraft() {
		SharedPreferencesUtils.putListData("draft_selectedList", selectedList.value!!)
		SharedPreferencesUtils.putData("draft_tittle", title.value!!)
		SharedPreferencesUtils.putData("draft_content", content.value!!)
	}
	
	fun clearDraft() {
		SharedPreferencesUtils.putListData("draft_selectedList", mutableListOf())
		SharedPreferencesUtils.putData("draft_tittle", "")
		SharedPreferencesUtils.putData("draft_content", "")
	}
	
	//
	fun release(releaseListener: ReleaseListener): Single<ResultModel<Map<String, String>>>? {
		if (judgeReleaseParams()) {
			return upLoadImageList(
				apiService,
				selectedList.value!!,
				object : QiNiuUtil.QiNiuUpLoadListener {
					override fun onSuccess(s: String) {
						//上传图片列表成功
						isLoading.postValue(false)
						progress.postValue(0)
						releaseListener.releaseSuccess(s)
					}
					override fun onProgress(percent: Int) {
						//进度更新
						progress.postValue(percent)
					}
				}).compose(dealLoading())
		}
		return null


/*		return apiService.releaseDynamic(
			ReleaseDynamicRequestModel(
				"!!!!我要发布动态!!!!"
			)
		).subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealLoading())
			.compose(dealErrorCode())
			.compose(dealError())*/
		
		//设置上传后文件的key
		
	}
	
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isLoading.postValue(true) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
	private fun judgeReleaseParams(): Boolean {
		if (title.value?.isNotEmpty()!!) {
			if (content.value?.isNotEmpty()!!) {
				return true
			} else {
				sendError(ErrorData(ErrorType.INPUT_ERROR, "说点什么吧～"))
			}
		} else {
			sendError(ErrorData(ErrorType.INPUT_ERROR, "添加一个标题吧～"))
		}
		return false
	}
}

data class GetQiNiuTokenRequestModel(val bucketName: String, val fileKeys: List<String>)

interface ReleaseListener {
	fun releaseSuccess(s: String)
}

