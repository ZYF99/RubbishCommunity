package com.example.rubbishcommunity.ui.home.message

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ChatService
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.model.Message
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance

class MessageViewModel(application: Application) : BaseViewModel(application) {
	private val chatService by instance<ChatService>()
	val messageList = MutableLiveData<List<Message>>()
	val isRefreshing = MutableLiveData(false)

	
	fun fetchMessageList(){
		chatService.getMessageList(0)
			.doOnSubscribe {
				val item = Message(
					"eqtwruy8",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"dsfsdf", "参与绿色行动，保护美丽家园。\n", "2019.8.21"
				)
				messageList.postValue(listOf(item, item, item, item))
			}
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