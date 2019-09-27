package com.example.rubbishcommunity.ui.home.find.dynamic

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.model.Dynamic
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class DynamicViewModel(application: Application) : BaseViewModel(application) {


    private val dynamicService by instance<ApiService>()

    val dynamicList = MutableLiveData<MutableList<Dynamic>>()

    val isRefreshing = MutableLiveData<Boolean>()


    fun init() {
        dynamicList.value = mutableListOf()
        getDynamicList()
    }


    fun getDynamicList() {
        val gridImgUrls: MutableList<String> = mutableListOf()
        gridImgUrls.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
        gridImgUrls.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
        gridImgUrls.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
        gridImgUrls.add("http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg")
        gridImgUrls.add("http://img1.imgtn.bdimg.com/it/u=1564534394,3601270978&fm=27&gp=0.jpg")
        gridImgUrls.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")


        val item = Dynamic(
            "dsfsdf", "参与绿色行动，保护美丽家园。\n" +
                    "\n" +
                    "Take part in green action to protect beautiful home.", gridImgUrls, "231234", "2019年7月30日", "23123"
        )
        val list = mutableListOf(item, item, item, item, item, item)

        dynamicList.value = list

/*        dynamicService.getDynamicList(0)
            .compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                dynamicList.value = it as MutableList<Dynamic>?
            }
            .compose(dealError())
            .bindLife()*/
        
        
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