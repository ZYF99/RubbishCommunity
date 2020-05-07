package com.example.rubbishcommunity.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.RelationService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.model.api.message.AddFriendsRequestModel
import com.example.rubbishcommunity.model.api.message.FetchAllLikeMeRequestListRequestModel
import com.example.rubbishcommunity.model.api.message.FetchAllLikeMeRequestListResultModel
import com.example.rubbishcommunity.model.api.message.UserMatchRelationRespListModel
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Flowable
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue


class MainViewModel(application: Application) : BaseViewModel(application) {
	
	val relationService by instance<RelationService>()
	private val userService by instance<UserService>()
	val appBarOffsetBias = MutableLiveData(0F)
	val requestList = MutableLiveData(
		emptyList<UserMatchRelationRespListModel.UserMatchRelationModel>()
	)
	val likeRequestData = MutableLiveData<FetchAllLikeMeRequestListResultModel>()
	
	//当fragment的appbar滑动时调用了此方法 ，这里应该改变BottomNavigationView的位置
	fun onAppBarOffsetChanged(verticalOffset: Int, appbarHeight: Float) {
		appBarOffsetBias.value = verticalOffset.absoluteValue.toFloat() / appbarHeight
	}
	
	//拉取所有好友请求
	fun fetchAllLikeRequest() {
		relationService.fetchAllLikeRequestList(FetchAllLikeMeRequestListRequestModel())
			.doOnApiSuccess {
				likeRequestData.postValue(it.data)
			}
	}
	
	//同意好友
	fun receiveFriend(openId: String) {
		userService.openIdToUin(openId)
			.flatMap {
				relationService.addFriends(
					AddFriendsRequestModel(it.data.uin)
				)
			}.dealLoading()
			.doOnApiSuccess {
				fetchAllLikeRequest()
			}
	}
	
	//拒绝好友
	fun refuseFriend(openId: String) {
		userService.openIdToUin(openId)
			.flatMap {
				relationService.addFriends(
					AddFriendsRequestModel(
						it.data.uin,
						relationType = 0
					)
				)
			}.dealLoading()
			.doOnApiSuccess {
				fetchAllLikeRequest()
			}
	}


/*	private val _parentKodein: Kodein by lazy { (application as MyApplication).kodein }
	
	override val kodein = Kodein {
		extend(_parentKodein, copy = Copy.All)
		import(mainModule)
	}
	
	val mqttClient by instance<MqttAndroidClient>()
	
	fun initMqtt(): Single<IMqttToken>{
		return mqttConnect(mqttClient)
			.doOnSuccess { MyApplication.showSuccess("MQTT连接成功") }
			.flatMap {
				mqttSubscribe(mqttClient)
			}.doOnSuccess {
				MyApplication
					.showSuccess("MQTT订阅成功")
				setDisconnectedBufferOptions(mqttClient)
			}
	}
	*/
	
}