package com.example.rubbishcommunity.ui.home.mine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.manager.api.MachineService
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.machine.BindMachineRequestModel
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.model.api.mine.UsrProfileResp
import com.example.rubbishcommunity.model.api.moments.GetMomentsByUinRequestModel
import com.example.rubbishcommunity.model.api.moments.GetMomentsResultModel
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.model.api.moments.PageParam
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.persistence.saveUserInfo
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.ui.home.mine.editinfo.getImageUrlFromServer
import com.example.rubbishcommunity.utils.switchThread
import com.example.rubbishcommunity.utils.upLoadImage
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import okhttp3.ResponseBody
import org.kodein.di.generic.instance

const val BACKGROUND_URL =
	"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583989375777&di=e0b2e3ec7dd59e4f3ec5d295fcc5abce&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201612%2F11%2F125901gs0055sdf10fzw1d.jpg"

class MineViewModel(application: Application) : BaseViewModel(application) {
	
	private val userService by instance<UserService>()
	private val momentService by instance<MomentService>()
	private val machineService by instance<MachineService>()
	private val imageService by instance<ImageService>()
	val machineBannerList =
		MutableLiveData(emptyList<FetchMachineInfoResultModel.MachineHeathInfo>())
	val recentMomentList = MutableLiveData(emptyList<MomentContent>())
	val userInfo = MutableLiveData<UsrProfile>()
	val isRefreshing = MutableLiveData<Boolean>()
	val isLoadingMore = MutableLiveData(false)
	private val isLastPage = MutableLiveData(false)
	private val startPage = MutableLiveData(1)

	
	fun refreshUserInfo() {
		//获取用户详细信息
		userInfo.postValue(getLocalUserInfo())
		Single.zip<ResultModel<UsrProfileResp>,
				ResultModel<GetMomentsResultModel>,
				Pair<UsrProfileResp, GetMomentsResultModel>>(
			userService.fetchUserProfile(),
			momentService.fetchMomentsByUin(
				GetMomentsByUinRequestModel(
					pageParamRequest = PageParam(
						1
					)
				)
			),
			BiFunction { f, s ->
				Pair(f.data, s.data)
			}
		).dealLoadingMore()
			.doOnApiSuccess {
				val profile =
					if (it.first.usrProfile.backgroundImage.isEmpty())
						it.first.usrProfile.copy(backgroundImage = BACKGROUND_URL)
					else it.first.usrProfile
				userInfo.postValue(profile)
				saveUserInfo(it.first.usrProfile)
				recentMomentList.postValue(it.second.momentContentList)
			}
		
		
		machineService.fetchMachineInfo()
			.dealRefreshing()
			.doOnApiSuccess {
				machineBannerList.postValue(it.data.machineHeathInfoList)
			}
	}
	
	fun bindMachine(
		bindKey: String,
		macAddress: String,
		nickName: String
	) {
		machineService.bindMachine(
			BindMachineRequestModel(
				bindKey,
				macAddress,
				nickName
			)
		).dealLoading()
			.doOnApiSuccess {
				if (it.meta.code == 1001) MyApplication.showSuccess(it.meta.msg)
				else MyApplication.showWarning(it.meta.msg)
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
					pageNum = startPage.value ?: 1
				)
			)
		).dealLoadingMore()
			.doOnApiSuccess {
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
	
	private fun <T> Single<T>.dealLoadingMore() =
		doOnSubscribe { isLoadingMore.postValue(true) }
			.doFinally { isLoadingMore.postValue(false) }
	
	
	private fun <T> editUserInfo(key: String, value: T) = userService.editUserInfo(
		hashMapOf(Pair(key, value.toString()))
	)
	
	
}
