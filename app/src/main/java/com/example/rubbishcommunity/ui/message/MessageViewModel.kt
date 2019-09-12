package com.example.rubbishcommunity.ui.message

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.base.BaseViewModel
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.Message
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance

class MessageViewModel(application: Application) : BaseViewModel(application), MessageListAdapter.Listener {



    override fun onCellClick() {

    }

    override fun onCellLongClick() {

    }



    private val dynamicService by instance<ApiService>()

    val messageList = MutableLiveData<MutableList<Message>>()

    val refreshing = MutableLiveData<Boolean>()


    fun init() {
        messageList.value = mutableListOf()
        getMessageList()
    }


    fun getMessageList() {

        val item = Message(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564501096061&di=a38d4fdc3cd3f8b8fc05d5aa1cea9f97&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137015b23fd90a8012034f7594657.jpg%401280w_1l_2o_100sh.jpg",
            "dsfsdf", "参与绿色行动，保护美丽家园。\n", "2019.8.21"
        )


        val list = mutableListOf(item, item, item, item, item, item, item, item, item, item, item, item)

        messageList.value = list

        dynamicService.getDynamicList(0)
            .compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                messageList.value = it as MutableList<Message>?
            }
            .compose(dealError())
            .bindLife()
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