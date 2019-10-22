package com.example.rubbishcommunity.ui.release.dynamic


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.*
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.ui.utils.ErrorData
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


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
	
	
	fun saveDraft() {
		if (title.value!!.isNotEmpty() || content.value!!.isNotEmpty()) {
			SharedPreferencesUtils.putListData("draft_selectedList", selectedList.value!!)
			SharedPreferencesUtils.putData("draft_tittle", title.value!!)
			SharedPreferencesUtils.putData("draft_content", content.value!!)
			MyApplication.showSuccess("已存入草稿箱～")
		} else {
			MyApplication.showWarning("添加内容后才能存草稿哦～")
		}
	}
	
	fun clearDraft() {
		SharedPreferencesUtils.putListData("draft_selectedList", mutableListOf())
		SharedPreferencesUtils.putData("draft_tittle", "")
		SharedPreferencesUtils.putData("draft_content", "")
	}
	
	//上传图片并发布动态
	fun release(releaseListener: ReleaseListener): Single<ResultModel<Map<String, String>>>? {
		if (judgeReleaseParams()) {
			if ((selectedList.value ?: listOf<String>()).isNotEmpty()) {
				//有图片
				//上传图片至七牛云
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
			} else {
				//没图片
				//直接发动态
				/*		return apiService.releaseDynamic(
							ReleaseDynamicRequestModel(
								"!!!!我要发布动态!!!!"
							)
					).subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.compose(dealLoading())
							.compose(dealErrorCode())
							.compose(dealError())*/
			}
			
			
		}
		return null
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
				sendError(
					ErrorData(
						ErrorType.INPUT_ERROR,
						"说点什么吧～"
					)
				)
			}
		} else {
			sendError(
				ErrorData(
					ErrorType.INPUT_ERROR,
					"添加一个标题吧～"
				)
			)
		}
		return false
	}
}

data class GetQiNiuTokenRequestModel(val bucketName: String, val fileKeys: List<String>)

interface ReleaseListener {
	fun releaseSuccess(s: String)
}

