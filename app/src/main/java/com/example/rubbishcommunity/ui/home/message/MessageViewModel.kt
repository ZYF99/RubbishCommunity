package com.example.rubbishcommunity.ui.home.message

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ChatService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.Message
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance

class MessageViewModel(application: Application) : BaseViewModel(application) {
	private val chatService by instance<ChatService>()
	val messageList = MutableLiveData<List<Message>>()
	val isRefreshing = MutableLiveData(false)

	
	fun fetchMessageList(){
		chatService.getMessageList(0)
			.doOnApiSuccess {
				messageList.postValue(it)
			}
	}
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs
				.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
	
}