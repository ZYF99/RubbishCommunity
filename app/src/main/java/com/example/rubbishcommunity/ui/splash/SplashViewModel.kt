package com.example.rubbishcommunity.ui.splash

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.base.ServerError
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.search.Category
import com.example.rubbishcommunity.persistence.*
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.ui.utils.*
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Observable
import io.reactivex.Single
import org.kodein.di.generic.instance


class SplashViewModel(application: Application) : BaseViewModel(application) {
	
	val imgUrl =
		MutableLiveData("http://img.zcool.cn/community/01f96455547f4e0000009af03b9eb8.jpg@2o.jpg")
	
	private val rubbishService by instance<RubbishService>()
	private val userService by instance<UserService>()
	
	
	@RequiresApi(Build.VERSION_CODES.O)
	@SuppressLint("UseSparseArrays")
	fun initClassification(): Single<String> {
		val categoryList = mutableListOf<Category>()
		
		return Single.create { resEmitter ->
			Single.create<Int> { emitter ->
				//获取用户新意并检验Token是否有效
				userService.getUserProfile()
					.switchThread()
					.doOnSuccess {
						saveUserInfo(it.data.usrProfile)
						emitter.onSuccess(1)
					}.doOnError {
						if (it is ServerError && it.code == 401) //没有权限
						{
							//尝试重新登陆
							userService.loginOrRegister(
								LoginOrRegisterRequestModel(
									LoginOrRegisterRequestModel.DeviceInfo(
										getVersionName(MyApplication.instance) ?: "",
										deviceBrand,
										getPhoneIMEI(MyApplication.instance),
										"Android",
										systemModel,
										systemVersion
									
									),
									0,
									getLocalPassword(),
									false,
									getLocalEmail()
								)
							).switchThread()
								.doOnSuccess { loginResult ->
									//持久化得到的token以及用户登录的信息
									saveVerifyInfo(
										getLocalEmail(),
										getLocalPassword(),
										loginResult.data.token,
										loginResult.data.openId,
										loginResult.data.usrStatusFlag.emailVerifiedFlag,
										loginResult.data.usrStatusFlag.needMoreInfoFlag
									)
									//存储用户个人信息
									saveUserInfo(
										loginResult.data.usrProfile
									)
									//登陆状态置为true
									saveLoginState(true)
									
									emitter.onSuccess(1)
								}
								.doOnError {
									emitter.onError(Throwable())
								}
								.compose(dealErrorCode())
								.compose(dealError())
								.bindLife()
						}
					}.compose(dealErrorCode())
					.compose(dealError())
					.bindLife()
			}.doOnSuccess {
				Observable.fromIterable(listOf(1, 2, 3, 4))
					.flatMapSingle { num ->
						rubbishService.searchCategoryByName(num)
					}.map {
						categoryList.add(it.data)
						categoryList
					}.switchThread()
					.doOnComplete {
						saveClassificationMap(categoryList)
						resEmitter.onSuccess("")
					}
					.doOnError {
						resEmitter.onError(Throwable())
					}.bindLife()
			}.bindLife()
		}
		
	}
	
}