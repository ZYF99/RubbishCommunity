package com.example.rubbishcommunity.ui.home.mine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.model.api.moments.GetMomentsByUinRequestModel
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.model.api.moments.PageParam
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.persistence.saveUserInfo
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.ui.home.mine.editinfo.getImageUrlFromServer
import com.example.rubbishcommunity.utils.switchThread
import com.example.rubbishcommunity.utils.upLoadImage
import io.reactivex.Single
import org.kodein.di.generic.instance

const val BACKGROUND_URL =
	"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583989375777&di=e0b2e3ec7dd59e4f3ec5d295fcc5abce&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201612%2F11%2F125901gs0055sdf10fzw1d.jpg"

class MineViewModel(application: Application) : BaseViewModel(application) {
	
	private val userService by instance<UserService>()
	private val momentService by instance<MomentService>()
	private val imageService by instance<ImageService>()
	val recentMomentList = MutableLiveData(emptyList<MomentContent>())
	val userInfo = MutableLiveData<UsrProfile>()
	val isRefreshing = MutableLiveData<Boolean>()
	private val isLastPage = MutableLiveData(false)
	private val startPage = MutableLiveData(1)
	val isLoadingMore = MutableLiveData(false)
	
	fun refreshUserInfo() {
		//获取用户详细信息
		userInfo.postValue(getLocalUserInfo())
		
		//获取用户详细信息
		userService.fetchUserProfile()
			.dealRefreshing()
			.doOnApiSuccess {
				val profile =
					if (it.data.usrProfile.backgroundImage.isEmpty())
						it.data.usrProfile.copy(backgroundImage = BACKGROUND_URL)
					else it.data.usrProfile
				userInfo.postValue(profile)
				saveUserInfo(it.data.usrProfile)
			}
		
		//获取最近动态列表
		momentService.fetchMomentsByUin(
			GetMomentsByUinRequestModel(
				pageParamRequest = PageParam(
					1,
					5
				)
			)
		).dealRefreshing()
			.doOnApiSuccess {
				recentMomentList.postValue(it.data.momentContentList)
			}
	}
	
	//修改背景图
	fun editBackground(imagePath: String) {
		imageService.upLoadImage(
			imagePath
		) {
			//onProgress
		}.flatMap { key ->
			userInfo.postValue(userInfo.value?.copy(backgroundImage = getImageUrlFromServer(key)))
			editUserInfo("backgroundImage", getImageUrlFromServer(key)).switchThread()
		}.doOnApiSuccess {
			MyApplication.showSuccess("修改成功")
		}
	}
	
	///加载更多
	fun loadMoreMoments() {
		momentService.fetchMomentsByUin(
			GetMomentsByUinRequestModel(
				pageParamRequest = PageParam(
					pageNum = startPage.value?:1
				)
			)
		).doOnApiSuccess {
				isLastPage.postValue(it.data.pageInfoResp.lastPage)
				if (it.data.momentContentList.isNotEmpty())
					recentMomentList.postValue(
						recentMomentList.value
							?.toMutableList()
							?.apply {
								addAll(it.data.momentContentList)
							})
				startPage.postValue(startPage.value!! + 1)
			}
	}

	
	private fun <T> Single<T>.dealRefreshing() =
		doOnSubscribe { isRefreshing.postValue(true) }
			.doFinally { isRefreshing.postValue(false) }
	
	
	private fun <T> editUserInfo(key: String, value: T) = userService.editUserInfo(
		hashMapOf(Pair(key, value.toString()))
	)
	
	
}
