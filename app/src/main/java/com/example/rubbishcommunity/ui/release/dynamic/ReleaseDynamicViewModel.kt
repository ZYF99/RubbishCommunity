package com.example.rubbishcommunity.ui.release.dynamic


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.*
import com.example.rubbishcommunity.manager.api.DynamicService
import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.release.draft.Draft
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/9/28 21:10
 */
class ReleaseDynamicViewModel(application: Application) : BaseViewModel(application) {
	//Toolbar标题栏
	val toolbarTitle = MutableLiveData("发布动态")
	
	//是否正在加载
	val isLoading = MutableLiveData(false)
	
	//已选图片列表
	val selectedList = MutableLiveData(mutableListOf<LocalMedia>())
	
	//标题
	val title = MutableLiveData("")
	
	//内容
	val content = MutableLiveData("")
	
	//位置
	val location = MutableLiveData<BDLocation>()
	
	//进度
	val progress = MutableLiveData(0)
	
	private val imageService by instance<ImageService>()
	private val dynamicService by instance<DynamicService>()
	
	fun init() {
		//必须初始化控件上的值
		
		getDraft()
	}
	
	
	private fun getDraft() {
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
		
		dynamicService.getDraft()
			.switchThread()
			.doOnSuccess {
				val draftData = it.data
				title.postValue(draftData.topic ?: "")
				content.postValue(draftData.dynamic ?: "")
				/*	selectedList.postValue(
						draftData.pictures
					)*/
			}.compose(dealLoading())
			.compose(dealErrorCode())
			.compose(dealError())
			.bindLife()
	}
	
	fun saveDraft() {
		if (title.value!!.isNotEmpty()) {
			if (content.value!!.isNotEmpty()) {
				SharedPreferencesUtils.putListData("draft_selectedList", selectedList.value!!)
				dynamicService.saveDraft(
					Draft(
						1,
						content.value!!,
						location.value?.latitude ?: 0.0,
						location.value?.longitude ?: 0.0,
						selectedList.value?.map {
							it.path
						}!!,
						title.value!!
					)
				).switchThread()
					.doOnSuccess {
						MyApplication.showSuccess("已存入草稿箱～")
					}.compose(dealLoading())
					.compose(dealErrorCode())
					.compose(dealError())
					.bindLife()
			} else {
				sendError(ErrorType.INPUT_ERROR,"添加内容后才能存草稿哦～")
			}
		} else {
			sendError(ErrorType.INPUT_ERROR,"添加标题后才能存草稿哦～")
		}
	}
	
	fun clearDraft() {
		SharedPreferencesUtils.putListData("draft_selectedList", mutableListOf())
		selectedList.value = mutableListOf()
		title.value = ""
		content.value = ""
		dynamicService
			.clearDraft()
			.switchThread()
			.compose(dealLoading())
			.compose(dealErrorCode())
			.compose(dealError())
			.bindLife()
	}
	
	//上传图片并发布动态
	fun release(onReleaseSuccess: (String) -> Unit): Single<ResultModel<Map<String, String>>>? {
		if (judgeReleaseParams()) {
			if (selectedList.value!!.isNotEmpty()) {
				//有图片
				//上传图片至七牛云
				return upLoadImageList(
					imageService,
					selectedList.value!!,
					{
						//上传图片列表成功
						isLoading.postValue(false)
						progress.postValue(0)
						onReleaseSuccess(it)
					}, {
						//进度更新
						progress.postValue(it)
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
			obs
				.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
	private fun judgeReleaseParams(): Boolean {
		if (title.value!!.isNotEmpty()) {
			if (content.value?.isNotEmpty()!!) {
				return true
			} else {
				sendError(
					ErrorType.INPUT_ERROR, "说点什么吧～"
				)
			}
		} else {
			sendError(
				ErrorType.INPUT_ERROR,
				"添加一个标题吧～"
			)
		}
		return false
	}
}


