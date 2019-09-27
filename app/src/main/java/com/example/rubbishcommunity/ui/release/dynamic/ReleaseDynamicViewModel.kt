package com.example.rubbishcommunity.ui.release.dynamic


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.initLocationOption
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


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
	
	//fun release(): Single<ResultModel<ReleaseDynamicResultModel>>? {
	fun release(): Single<Int>? {
/*		return apiService.releaseDynamic(
			ReleaseDynamicRequestModel(
				"!!!!我要发布动态!!!!"
			)
		).subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealLoading())
			.compose(dealErrorCode())
			.compose(dealError())*/
		return Single.just(1).delay(5, TimeUnit.SECONDS).compose(dealLoading())
		
	}
	
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
}