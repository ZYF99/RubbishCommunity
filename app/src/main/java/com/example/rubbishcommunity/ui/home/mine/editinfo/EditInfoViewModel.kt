package com.example.rubbishcommunity.ui.home.mine.editinfo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class EditInfoViewModel(application: Application) : BaseViewModel(application) {
	
	private val userService by instance<UserService>()
	
	val toolbarTitle = MutableLiveData("编辑资料")
	
	val avatar = MutableLiveData("")
	val name = MutableLiveData("默认昵称")
	val gender = MutableLiveData("男")
	val birthString = MutableLiveData("1998年10月10日")
	val signature = MutableLiveData("")
	val profession = MutableLiveData("")
	val aboutMe = MutableLiveData("")
	
	val isRefreshing = MutableLiveData(false)
	val isUpdated = MutableLiveData(false)
	
	fun editName() {
		userService.editInfo(
			hashMapOf(
				Pair("name", name.value!!)
			)
		).switchThread()
			.doOnSuccess {
				isUpdated.postValue(true)
			}.compose(dealErrorCode())
			.compose(dealError())
			.compose(dealRefreshing())
			.bindLife()
	}
	
	fun initData() {
		//初始化原始信息
		getLocalUserInfo().run {
			this@EditInfoViewModel.avatar.postValue(avatar)
			this@EditInfoViewModel.name.postValue(name)
			this@EditInfoViewModel.gender.postValue(gender)
			this@EditInfoViewModel.birthString.postValue(birthday)
			this@EditInfoViewModel.signature.postValue(signature)
			this@EditInfoViewModel.profession.postValue(profession)
			this@EditInfoViewModel.aboutMe.postValue(aboutMe)
		}
	}
	
	
	private fun <T> dealRefreshing(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
}
