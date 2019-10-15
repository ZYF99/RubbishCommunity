package com.example.rubbishcommunity.ui.home.message.chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.ChatMessage
import com.example.rubbishcommunity.persistence.getLocalOpenId
import com.example.rubbishcommunity.utils.getNowString
import org.kodein.di.generic.instance

class ChatViewModel(application: Application) : BaseViewModel(application) {
	private val apiService by instance<ApiService>()
	
	val toolbarTitle = MutableLiveData<String>()
	val chatList = MutableLiveData<MutableList<ChatMessage>>()
	
	val inputMsg = MutableLiveData<String>()
	
	
	fun init(openId: String) {
		toolbarTitle.value = openId
		chatList.value = mutableListOf()
		getChatList()
	}
	
	fun getChatList() {
		
		val item = ChatMessage(
			"Zhangyf",
			"https://pics7.baidu.com/feed/0df3d7ca7bcb0a4661674f964cee6e216a60af6f.jpeg?token=56029471cf078b669a5a60f8c51c9b2e&s=965371C8DF31B647505B0C900300F08B",
			"dsfsdf",
			"2019.8.21"
		)
		val list = mutableListOf(item, item, item, item)
		chatList.value = list
/*        dynamicService.getDynamicList(0)
            .compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                messageList.value = it as MutableList<Message>?
            }
            .compose(dealError())
            .bindLife()*/
	}
	
	fun sendStringMsg() {
		chatList.value?.add(
			ChatMessage(
				getLocalOpenId(),
				"https://pics7.baidu.com/feed/0df3d7ca7bcb0a4661674f964cee6e216a60af6f.jpeg?token=56029471cf078b669a5a60f8c51c9b2e&s=965371C8DF31B647505B0C900300F08B",
				inputMsg.value ?: "",
				getNowString()
			)
		)
	}
	
}