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
import com.qiniu.android.storage.UploadManager
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit
import com.qiniu.android.storage.UploadOptions
import com.example.rubbishcommunity.persistence.getLocalToken
import com.qiniu.android.storage.UpProgressHandler


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
	
	//fun release(): Single<ResultModel<ReleaseDynamicResultModel>>? {
	fun release(): Single<String>? {
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
		val upkey = selectedList.value!![0].path
		return Single.just("Mt6fBM0NAN4FIYLDQGOkoCse09OTdqEIELdLmx_z:9sE2cFSMIEkJEClFKyXksIiUmcY=:eyJzY29wZSI6IkRldyIsInBlcnNpc3RlbnRPcHMiOiJpbWFnZVZpZXcyLzEvdy84MC9oLzgwfHNhdmVhcy9SR1YzT2xwb1lXNW5lV1pVWlhOME1ERmZPREF1YW5CbjtpbWFnZVZpZXcyLzEvdy8xNDAvaC8xNDB8c2F2ZWFzL1JHVjNPbHBvWVc1bmVXWlVaWE4wTURGZk1UUXdMbXB3WndcdTAwM2RcdTAwM2Q7aW1hZ2VWaWV3Mi8xL3cvMTYwL2gvMTYwfHNhdmVhcy9SR1YzT2xwb1lXNW5lV1pVWlhOME1ERmZNVFl3TG1wd1p3XHUwMDNkXHUwMDNkO2ltYWdlVmlldzIvMS93LzIyMC9oLzIyMHxzYXZlYXMvUkdWM09scG9ZVzVuZVdaVVpYTjBNREZmTWpJd0xtcHdad1x1MDAzZFx1MDAzZDtpbWFnZVZpZXcyLzEvdy80NDAvaC80NDB8c2F2ZWFzL1JHVjNPbHBvWVc1bmVXWlVaWE4wTURGZk5EUXdMbXB3WndcdTAwM2RcdTAwM2Q7aW1hZ2VWaWV3Mi8xL3cvNjQwL2gvNjQwfHNhdmVhcy9SR1YzT2xwb1lXNW5lV1pVWlhOME1ERmZOalF3TG1wd1p3XHUwMDNkXHUwMDNkOyIsImRlYWRsaW5lIjoxNTcwNTMyNzYwfQ==")
			.delay(2, TimeUnit.SECONDS)
			.doOnSuccess {
				UploadManager().put(
					selectedList.value!![0].path, upkey, it,
					{ key, rinfo, response ->
						val s = "$key, $rinfo, $response"
						content.postValue("七牛上传测试 $s")
					}, UploadOptions(null, "test-type", true,
						UpProgressHandler { key, percent ->
							progress.postValue(percent.toInt())
						}, null
					)
				)
			}.compose(dealLoading())
	}
	
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
}