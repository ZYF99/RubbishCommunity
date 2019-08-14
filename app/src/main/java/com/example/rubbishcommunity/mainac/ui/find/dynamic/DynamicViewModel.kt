package com.example.rubbishcommunity.mainac.ui.find.dynamic

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.fenrir_stage4.manager.api.DynamicService
import com.example.rubbishcommunity.base.BaseViewModel
import com.example.rubbishcommunity.model.Dynamic
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class DynamicViewModel(application: Application) : BaseViewModel(application) {


    private val dynamicService by instance<DynamicService>()

    val dynamicList = MutableLiveData<MutableList<Dynamic>>()

    val refreshing = MutableLiveData<Boolean>()


    fun init() {
        dynamicList.value = mutableListOf()
        getDynamicList()

    }


    fun getDynamicList() {
        val bannerUrls: MutableList<String> = mutableListOf()
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564501096061&di=a38d4fdc3cd3f8b8fc05d5aa1cea9f97&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137015b23fd90a8012034f7594657.jpg%401280w_1l_2o_100sh.jpg")
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564501096061&di=a38d4fdc3cd3f8b8fc05d5aa1cea9f97&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137015b23fd90a8012034f7594657.jpg%401280w_1l_2o_100sh.jpg")
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564501096061&di=a38d4fdc3cd3f8b8fc05d5aa1cea9f97&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137015b23fd90a8012034f7594657.jpg%401280w_1l_2o_100sh.jpg")


        val item = Dynamic(
            "dsfsdf", "参与绿色行动，保护美丽家园。\n" +
                    "\n" +
                    "Take part in green action to protect beautiful home.", bannerUrls, "231234", "2019年7月30日", "23123"
        )
        val list = mutableListOf(item, item, item, item, item, item)

        dynamicList.value = list

        dynamicService.getDynamicList(0)
            //.compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                dynamicList.value = it as MutableList<Dynamic>?
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