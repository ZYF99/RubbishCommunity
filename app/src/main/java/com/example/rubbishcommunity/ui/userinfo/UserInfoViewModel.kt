package com.example.rubbishcommunity.ui.userinfo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance

class UserInfoViewModel(application: Application) : BaseViewModel(application) {
	
	val isLoading = MutableLiveData(false)
	private val userService by instance<UserService>()
	private val momentService by instance<MomentService>()
	val userInfo = MutableLiveData<UsrProfile>()
	
	fun fetchUserInfo(openId: String?) {
		userService.fetchUserInfoByOpenId(openId)
			.doOnApiSuccess {
				userInfo.postValue(it.data.usrProfile)
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
	
	
}