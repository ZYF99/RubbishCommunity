package com.example.rubbishcommunity.ui.home.mine

import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.ui.BaseViewModel
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class MineViewModel(application: Application) : BaseViewModel(application) {
	
	private val apiService by instance<ApiService>()
	
	val userInfo = MutableLiveData<LoginOrRegisterResultModel.UsrProfile>()
	val isRefreshing = MutableLiveData<Boolean>()
	
	
	fun getUserInfo() {
		val usrProfile = SharedPreferencesUtils.getData(
			"localUsrProfile",
			LoginOrRegisterResultModel.UsrProfile.getNull()
		)
		if (usrProfile != null)
			userInfo.postValue(
				usrProfile as LoginOrRegisterResultModel.UsrProfile
			)
		//从网络获取个人信息数据
/*		apiService
			.getUserInfo(getLocalUserName())
			.compose(dealErrorCode())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealRefreshing())
			.compose(dealError())
			.doOnSuccess {
				userInfo.postValue(it.data)
			}
			.bindLife()*/
		
		
	}
	
	fun logout(): Single<ResultModel<String>> {
		return apiService.logout()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealError())
		
	}
	
	private fun <T> dealRefreshing(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe {
				isRefreshing.postValue(true)
			}
				.doOnSuccess {
					isRefreshing.postValue(false)
				}
				.doOnError {
					isRefreshing.postValue(false)
				}
		}
	}
}

@BindingAdapter("gender")
fun getGenderDrawable(imageView: ImageView, gender: Int?) {
	
	when (gender) {
		3 -> {
			Glide.with(imageView.context).load(R.drawable.icon_gendermale)
				.placeholder(R.mipmap.ic_launcher)
				.dontAnimate()
				.into(imageView)
		}
		else -> {
			Glide.with(imageView.context).load(R.drawable.icon_genderfemale)
				.placeholder(R.mipmap.ic_launcher)
				.dontAnimate()
				.into(imageView)
		}
	}
	
	
}

