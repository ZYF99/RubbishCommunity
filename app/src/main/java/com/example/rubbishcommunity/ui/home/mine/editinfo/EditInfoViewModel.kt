package com.example.rubbishcommunity.ui.home.mine.editinfo

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import com.example.rubbishcommunity.utils.upLoadImage
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


class EditInfoViewModel(application: Application) : BaseViewModel(application) {
	
	private val userService by instance<UserService>()
	private val imageService by instance<ImageService>()
	
	val toolbarTitle = MutableLiveData("编辑资料")
	
	val uploadProgress = MutableLiveData(0)
	val avatar = MutableLiveData("")
	val name = MutableLiveData("默认昵称")
	val gender = MutableLiveData("男")
	val birthday = MutableLiveData<Long>(0)
	val signature = MutableLiveData("")
	val profession = MutableLiveData("")
	val aboutMe = MutableLiveData("")
	
	val isRefreshing = MutableLiveData(false)
	
	//修改头像
	fun editAvatar() {
		imageService.upLoadImage(
			avatar.value!!
		) {
			//onProgress
			Log.d("PROGRESS", uploadProgress.value!!.toString())
			uploadProgress.postValue(it)
		}.flatMap { key ->
			avatar.postValue(key)
			userService.editUserInfo(hashMapOf(Pair("avatar", key)))
				.switchThread()
		}.compose(dealErrorCode())
			.compose(dealError())
			.compose(dealRefreshing())
			.bindLife()
	}
	
	//修改昵称
	fun editName() {
		editUserInfo("name", name.value!!)
			.doOnSuccess {}
			.bindLife()
	}
	
	//修改性别
	fun editGender() {
		editUserInfo("gender", gender.value!!)
			.doOnSuccess {}
			.bindLife()
	}
	
	//修改生日
	@SuppressLint("SimpleDateFormat")
	fun editBirthDay() {
		editUserInfo("birthday", birthday.toString())
			.bindLife()
	}
	
	//修改个人签名
	fun editSignature() {
		editUserInfo("signature", signature.value!!)
			.doOnSuccess {}
			.bindLife()
	}
	
	//修改个人签名
	fun editProfession() {
		editUserInfo("profession", profession.value!!)
			.doOnSuccess {}
			.bindLife()
	}
	
	//修改分类宣言
	fun editAboutMe() {
		editUserInfo("aboutMe", aboutMe.value!!)
			.doOnSuccess {}
			.bindLife()
	}
	
	fun initData() {
		//初始化原始信息
		getLocalUserInfo().run {
			this@EditInfoViewModel.avatar.postValue(avatar)
			this@EditInfoViewModel.name.postValue(name)
			this@EditInfoViewModel.gender.postValue(gender)
			this@EditInfoViewModel.birthday.postValue(birthday)
			this@EditInfoViewModel.signature.postValue(signature)
			this@EditInfoViewModel.profession.postValue(profession)
			this@EditInfoViewModel.aboutMe.postValue(aboutMe)
		}
	}
	
	private fun <T> editUserInfo(key: String, value: T): Single<ResultModel<String>> {
		return userService.editUserInfo(
			hashMapOf(Pair(key, value.toString()))
		).switchThread()
			.compose(dealErrorCode())
			.compose(dealError())
			.compose(dealRefreshing())
		
	}
	
	
	private fun <T> dealRefreshing(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
}
