package com.example.rubbishcommunity.ui.release.moments


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.model.api.release.ReleaseMomentRequestModel
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.release.draft.Draft
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance
import timber.log.Timber

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/9/28 21:10
 */
class ReleaseMomentViewModel(application: Application) : BaseViewModel(application) {
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
	private val momentService by instance<MomentService>()
	
	fun getDraft() {
		//获取本地的草稿图片列表
		selectedList.value = SharedPreferencesUtils.getListData(
			"draft_selectedList",
			LocalMedia::class.java
		)
		//获取草稿
		momentService.getDraft()
			.compose(dealLoading())
			.doOnApiSuccess {
				val draftData = it.data
				title.postValue(draftData?.title ?: "")
				content.postValue(draftData?.content ?: "")
			}
	}
	
	private fun getSaveDraftSingle(selectedImgList: List<String>? = selectedList.value?.map { it.path }) =
		momentService.saveDraft(
			Draft(
				1,
				content.value ?: "",
				location.value?.latitude ?: 0.0,
				location.value?.longitude ?: 0.0,
				selectedImgList ?: emptyList(),
				title.value!!,
				"default_topic"
			)
		)
	
	
	fun saveDraft(onSavedAction: () -> Unit) {
		when {
			title.value?.isEmpty() ?: true -> {
				sendError(ErrorType.UI_ERROR, "添加个标题吧～")
				return
			}
			content.value?.isEmpty() ?: true -> {
				sendError(ErrorType.UI_ERROR, "要先写点什么内容哦～")
				return
			}
		}
		SharedPreferencesUtils.putListData("draft_selectedList", selectedList.value!!)
		getSaveDraftSingle()
			.compose(dealLoading())
			.doOnApiSuccess {
				onSavedAction.invoke()
			}
	}
	
	
	fun clearDraft() {
		SharedPreferencesUtils.putListData(
			"draft_selectedList",
			emptyList<String>().toMutableList()
		)
		selectedList.value = mutableListOf()
		title.value = ""
		content.value = ""
		momentService
			.clearDraft()
			.doOnApiSuccess {
			
			}
	}
	
	//上传图片并发布动态
	fun release(onReleaseAction: () -> Unit) {
		
		fun getUploadImageListSingle() = imageService.upLoadImageList(
			selectedList.value!!
		) {
			//进度更新
			progress.postValue(it)
		}
		
		fun getReleaseMomentSingle(momentsId: Long) = momentService.releaseMoment(
			ReleaseMomentRequestModel(
				location.value?.latitude,
				location.value?.longitude,
				momentsId
			)
		)
		
		fun getSaveDraftAndReleaseSingle(resultKeyList: List<String>? = null) =
			getSaveDraftSingle(resultKeyList)
				.flatMap {
					//存草稿成功，上传动态
					getReleaseMomentSingle(it.data.momentsId)
				}.switchThread()
		
		if (selectedList.value!!.isNotEmpty()) {
			//有图片,上传图片至七牛云
			getUploadImageListSingle()
				.flatMap { resultKeyList ->
					//todo fix this
					//上传图片列表成功
					//开始保存草稿并上传动态
					Timber.d("!!!!!$resultKeyList")
					getSaveDraftAndReleaseSingle(resultKeyList)
				}
		} else {
			//没有图片，直接存草稿
			getSaveDraftAndReleaseSingle()
		}.doOnApiSuccess {
			onReleaseAction.invoke()
		}
	}
	
	private fun <T> dealLoading(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
	}
	
}


