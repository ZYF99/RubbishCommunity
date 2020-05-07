package com.example.rubbishcommunity.ui.home.message.chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ChatService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.ChatMessage
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.model.api.message.SendChatMessageRequestModel
import com.example.rubbishcommunity.persistence.getLocalOpenId
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import java.util.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/7/22 20:10
 */
class ChatViewModel(application: Application) : BaseViewModel(application) {
	private val chatService by instance<ChatService>()
	private val userService by instance<UserService>()
	
	val toolbarTitle = MutableLiveData<String>()
	val chatList = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
	val receiver = MutableLiveData<SimpleProfileResp>()
	val inputMsg = MutableLiveData<String>()
	
	
	fun init() {
		toolbarTitle.value = receiver.value?.name
		chatList.value = mutableListOf()
		getChatList()
	}
	
	private fun getChatList() {
		
		val item1 = ChatMessage(
			receiver.value?.openId?:"",
			receiver.value?.avatar?:"",
			"你好",
			Date().time
		)
		
		val item2 = ChatMessage(
			receiver.value?.openId?:"",
			receiver.value?.avatar?:"",
			"哈哈哈哈哈哈",
			Date().time + 2000000
		)
		
		
		val list = mutableListOf(item1,item2)
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
	
	//发消息
	fun sendStringMsg() {
		
		chatList.value = chatList.value?.apply {
			add(
				ChatMessage(
					getLocalOpenId(),
					getLocalUserInfo().avatar,
					inputMsg.value ?: "",
					Date().time
				)
			)
		}
		inputMsg.value = ""
/*		userService.openIdToUin(receiver.value?.openId)
			.flatMap {
				chatService.sendMsgToPeople(
					SendChatMessageRequestModel(
						content = inputMsg.value ?: "",
						receiver = it.data.uin
					)
				)
			}.doOnApiSuccess {
				inputMsg.value = ""
			}*/
		
		
	}
	
	
}