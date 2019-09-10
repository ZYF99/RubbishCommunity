package com.example.rubbishcommunity.mainac.ui.homepage

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class HomepageViewModel(application: Application) : BaseViewModel(application) {


    private val apiService by instance<ApiService>()

    private val hotWordList = MutableLiveData<MutableList<String>>()



    fun init() {
        hotWordList.value = mutableListOf()
        getHotWordList()
    }



    private fun getHotWordList() {

        apiService.getHotWordList(0)
           // .compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                hotWordList.value = it as MutableList<String>?
            }
            .compose(dealError())
            .bindLife()
    }




/*    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs
                .doOnSubscribe { refreshing.postValue(true) }
                .doOnSuccess { refreshing.postValue(false) }
                .doOnError { refreshing.postValue(false) }
        }
    }*/



}