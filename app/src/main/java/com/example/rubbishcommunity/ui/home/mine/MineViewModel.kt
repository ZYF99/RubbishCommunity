package com.example.rubbishcommunity.ui.home.mine
import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.mine.UserCardResultModel
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.persistence.getLocalUserName
import com.example.rubbishcommunity.ui.BaseViewModel
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class MineViewModel(application: Application) : BaseViewModel(application) {
	
	private val apiService by instance<ApiService>()
	
	val userInfo = MutableLiveData<LoginOrRegisterResultModel.UsrProfile>()
	val userCard = MutableLiveData<UserCardResultModel>()
	val isRefreshing = MutableLiveData<Boolean>()
	
	
	fun getUserInfo(): Single<ResultModel<UserCardResultModel>> {
		val usrProfile = SharedPreferencesUtils.getData(
			"localUsrProfile",
			LoginOrRegisterResultModel.UsrProfile.getNull()
		)
		if (usrProfile != null)
			userInfo.postValue(
				usrProfile as LoginOrRegisterResultModel.UsrProfile
			)
		
		//获取用户卡片
		fun getUserCard(): Single<ResultModel<UserCardResultModel>> {
			return apiService
				.getUserCard(getLocalUserName())
				.compose(dealErrorCode())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(dealRefreshing())
				.compose(dealError())
				.doOnSuccess {
					userCard.postValue(it.data)
				}
		}


/*		//从网络获取个人信息数据
		apiService
			.getUserInfo(getLocalUserName())
			.subscribeOn(Schedulers.io())
			.flatMap { userInfoResultModel ->
				userInfo.postValue(userInfoResultModel.data)
				//成功后去获取卡片信息
				getUserCard()
			}
			.bindLife()*/
		
		//直接获取卡片数据
		return getUserCard()
		
		
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



@BindingAdapter("portrait")
fun getPortrait(imageView: ImageView, portraitUrl: String?) {
	Glide.with(imageView.context).load(portraitUrl)
		.placeholder(R.drawable.bg_404)
		.dontAnimate()
		.into(imageView)
}