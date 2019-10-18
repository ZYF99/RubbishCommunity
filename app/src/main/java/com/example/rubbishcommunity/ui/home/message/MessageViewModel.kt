package com.example.rubbishcommunity.ui.home.message

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.model.Message
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance

class MessageViewModel(application: Application) : BaseViewModel(application) {
	
	
	private val dynamicService by instance<ApiService>()
	
	val messageList = MutableLiveData<MutableList<Message>>()
	
	val refreshing = MutableLiveData<Boolean>()
	
	
	fun getMessageList(): Single<MutableList<Message>> {
		

		
		return dynamicService.getMessageList(0)
			.compose(dealRefresh())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSuccess {
				messageList.value = it
			}.doOnSubscribe {
				val item = Message(
					"eqtwruy8",
					"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564501096061&di=a38d4fdc3cd3f8b8fc05d5aa1cea9f97&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137015b23fd90a8012034f7594657.jpg%401280w_1l_2o_100sh.jpg",
					"dsfsdf", "参与绿色行动，保护美丽家园。\n", "2019.8.21"
				)
				messageList.value = mutableListOf(item, item, item, item)
			}
			.compose(dealError())
	}
	
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs
				.doOnSubscribe { refreshing.postValue(true) }
				.doOnSuccess { refreshing.postValue(false) }
				.doOnError { refreshing.postValue(false) }
		}
	}
	
}