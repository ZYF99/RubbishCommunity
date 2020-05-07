package com.example.rubbishcommunity.ui.home.message

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ChatService
import com.example.rubbishcommunity.manager.api.RelationService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.Message
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.model.api.message.FetchChatMessageListRequestModel
import com.example.rubbishcommunity.model.api.message.UserMatchRelationRespListModel
import io.reactivex.Single
import org.kodein.di.generic.instance
import java.util.*

class MessageViewModel(application: Application) : BaseViewModel(application) {
	private val chatService by instance<ChatService>()
	private val relationService by instance<RelationService>()
	val messageList = MutableLiveData<List<Message>>()
	val friendsList = MutableLiveData<List<UserMatchRelationRespListModel.UserMatchRelationModel>>()
	val isRefreshing = MutableLiveData(false)
	
	//拉取好友列表
	fun fetchFriendsList() {
		relationService.fetchFriendsList()
			.dealRefresh()
			.doOnApiSuccess {
				friendsList.postValue(it.data.usrMatchRelationRespList)
			}
	}
	
	//拉取聊天信息列表
	fun fetchMessageList() {
		val item = Message(
			uid = "sadfsaf",
			iconUrl = "https://profile.csdnimg.cn/3/D/7/3_agsdfgdfhdf",
			title = "哈哈哈",
			msg = "哈哈哈",
			time = "2020年5月8日 9:20",
			user = SimpleProfileResp.getDefault()
		)
		messageList.postValue(listOf(item))
		
/*		chatService.fetchChatMessageList(FetchChatMessageListRequestModel(
		
		))
			.doOnApiSuccess {
				//messageList.postValue(it)
			}*/
	}
	
	private fun <T> Single<T>.dealRefresh() =
		doOnSubscribe { isRefreshing.postValue(true) }
			.doOnSuccess { isRefreshing.postValue(false) }
			.doOnError { isRefreshing.postValue(false) }
	
	
}