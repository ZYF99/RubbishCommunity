package com.example.rubbishcommunity.ui.home.mine.editinfo

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.ui.utils.userInfoHasChanged
import com.example.rubbishcommunity.utils.switchThread
import com.example.rubbishcommunity.utils.upLoadImage
import io.reactivex.Single
import okhttp3.ResponseBody
import org.kodein.di.generic.instance

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
	val isUpdating = MutableLiveData(false)
	
	//修改头像
	fun editAvatar() {
		imageService.upLoadImage(
			avatar.value!!
		) {
			//onProgress
			uploadProgress.postValue(it)
		}.doOnSubscribe { isUpdating.postValue(true) }
			.doFinally { isUpdating.postValue(false) }
			.flatMap { key ->
				val url = getImageUrlFromServer(key)
				avatar.postValue(url)
				editUserInfo("avatar", url)?.switchThread()
			}?.doOnApiSuccess {
			
			}
		
	}
	
	//修改昵称
	fun editName() {
		editUserInfo("name", name.value!!)?.doOnApiSuccess {
		
		}
	}
	
	//修改性别
	fun editGender() {
		editUserInfo("gender", gender.value!!)
			?.doOnApiSuccess {
			
			}
	}
	
	//修改生日
	@SuppressLint("SimpleDateFormat")
	fun editBirthDay() {
		editUserInfo("birthday", birthday.value.toString())?.doOnApiSuccess {
		
		}
	}
	
	//修改个人签名
	fun editSignature() {
		editUserInfo("signature", signature.value!!)?.doOnApiSuccess {
		
		}
	}
	
	//修改个人签名
	fun editProfession() {
		editUserInfo("profession", profession.value!!)?.doOnApiSuccess {
		
		}
	}
	
	//修改分类宣言
	fun editAboutMe() {
		editUserInfo("aboutMe", aboutMe.value!!)?.doOnApiSuccess {
		
		}
	}
	
	//注销
	fun logout(): Single<ResponseBody> {
		return userService.logout()
			.switchThread()
			.catchApiError()
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
	
	private fun <T> editUserInfo(key: String, value: T): Single<ResultModel<String>>? {
		return userService.editUserInfo(
			hashMapOf(Pair(key, value.toString()))
		).dealLoading()
			.doOnSuccess {
				userInfoHasChanged = true
			}
		
	}
	
}

fun getImageUrlFromServer(key: String) = "http://image.upuphub.com/$key"