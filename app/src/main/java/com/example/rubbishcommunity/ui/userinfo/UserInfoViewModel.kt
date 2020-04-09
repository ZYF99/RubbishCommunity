package com.example.rubbishcommunity.ui.userinfo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.model.api.moments.GetMomentsByClassifyRequestModel
import com.example.rubbishcommunity.model.api.moments.GetMomentsByUinRequestModel
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.model.api.moments.PageParam
import io.reactivex.Single
import org.kodein.di.generic.instance

class UserInfoViewModel(application: Application) : BaseViewModel(application) {
	
	val isRefreshing = MutableLiveData(false)
	val isLoadingMore = MutableLiveData(false)
	private val userService by instance<UserService>()
	private val momentService by instance<MomentService>()
	val userInfo = MutableLiveData<UsrProfile>()
	val momentList = MutableLiveData<List<MomentContent>>()
	private val isLastPage = MutableLiveData(false)
	private val startPage = MutableLiveData(1)
	
	fun fetchUserInfo(openId: String?) {
		userService.fetchUserProfile(openId)
			.dealLoading()
			.doOnApiSuccess {
				userInfo.postValue(it.data.usrProfile)
			}
	}
	
	fun refreshRecentMoments(openId: String?) {
		momentService.fetchMomentsByUin(GetMomentsByUinRequestModel(
			openId = openId,
			pageParamRequest = PageParam(
				pageNum = startPage.value?:1
			)
		)).dealLoading()
			.doOnApiSuccess {
				momentList.postValue(it.data.momentContentList)
			}
	}
	
	
	///加载更多
	fun loadMoreMoments(openId: String?) {
		momentService.fetchMomentsByUin(
			GetMomentsByUinRequestModel(
				openId = openId,
				pageParamRequest = PageParam(
					pageNum = startPage.value?:1
				)
			)
		).dealLoading()
			.doOnApiSuccess {
				isLastPage.postValue(it.data.pageInfoResp.lastPage)
				if (it.data.momentContentList.isNotEmpty())
					momentList.postValue(
						momentList.value
							?.toMutableList()
							?.apply {
								addAll(it.data.momentContentList)
							})
				startPage.postValue(startPage.value!! + 1)
			}
	}
	
	//刷新机制
	private fun <T> Single<T>.dealLoading() =
		doOnSubscribe {
			isRefreshing.postValue(true)
		}
			.doOnSuccess {
				isRefreshing.postValue(false)
			}
			.doOnError {
				isRefreshing.postValue(false)
			}
	
	
}