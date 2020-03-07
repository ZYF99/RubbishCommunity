package com.example.rubbishcommunity.ui.home.message.chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ChatService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.ChatMessage
import com.example.rubbishcommunity.persistence.getLocalOpenId
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
	
	val toolbarTitle = MutableLiveData<String>()
	val chatList = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
	
	val inputMsg = MutableLiveData<String>()
	
	
	fun init(openId: String) {
		toolbarTitle.value = openId
		chatList.value = mutableListOf()
		getChatList()
	}
	
	private fun getChatList() {
		
		val item12 = ChatMessage(
			"Zhangyf",
			"https://pics7.baidu.com/feed/0df3d7ca7bcb0a4661674f964cee6e216a60af6f.jpeg?token=56029471cf078b669a5a60f8c51c9b2e&s=965371C8DF31B647505B0C900300F08B",
			"dsfsdf",
			1568436160000 //2019-10-14 12:42:40
		)
		
		val item0 = ChatMessage(
			"Zhangyf",
			"https://pics7.baidu.com/feed/0df3d7ca7bcb0a4661674f964cee6e216a60af6f.jpeg?token=56029471cf078b669a5a60f8c51c9b2e&s=965371C8DF31B647505B0C900300F08B",
			"dsfsdf",
			1571028160000 //2019-10-14 12:42:40
		)
		
		val item1 = ChatMessage(
			"Zhangyf",
			"https://pics7.baidu.com/feed/0df3d7ca7bcb0a4661674f964cee6e216a60af6f.jpeg?token=56029471cf078b669a5a60f8c51c9b2e&s=965371C8DF31B647505B0C900300F08B",
			"dsfsdf",
			1571114560000 //2019-10-15 12:42:40
		)
		val item2 = ChatMessage(
			"Zhangyf",
			"https://pics7.baidu.com/feed/0df3d7ca7bcb0a4661674f964cee6e216a60af6f.jpeg?token=56029471cf078b669a5a60f8c51c9b2e&s=965371C8DF31B647505B0C900300F08B",
			"dsfsdf",
			1571200960000 //2019-10-16 12:42:40
		)
		
		val list = mutableListOf(item12,item0, item1, item2)
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
	
	fun sendStringMsg():Single<String> {
		
		return chatService.sendMsgToPeople(inputMsg.value?:"")
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe {
				chatList.value = chatList.value?.run {
					add(
						ChatMessage(
							getLocalOpenId(),
							"https://pics7.baidu.com/feed/0df3d7ca7bcb0a4661674f964cee6e216a60af6f.jpeg?token=56029471cf078b669a5a60f8c51c9b2e&s=965371C8DF31B647505B0C900300F08B",
							inputMsg.value ?: "",
							Date().time
						)
					)
					chatList.value
				}
				inputMsg.value = ""
			}
			//.compose(dealLoading())
			.compose(dealErrorCode())
			.compose(catchApiError())
		}
	
}