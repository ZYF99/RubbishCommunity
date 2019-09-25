package com.example.rubbishcommunity.ui.home.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.ui.BaseViewModel
import org.kodein.di.generic.instance


class SearchViewModel(application: Application) : BaseViewModel(application) {


    private val apiService by instance<ApiService>()

    public val hotWordList = MutableLiveData<MutableList<String>>()
    public val searchWord = MutableLiveData<String>()



    fun init() {
        hotWordList.value = mutableListOf()
        searchWord.postValue("")
        getHotWordList()
    }



    private fun getHotWordList() {

/*        apiService.getHotWordList(0)
           // .compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                hotWordList.value = it as MutableList<String>?
            }
            .compose(dealError())
            .bindLife()*/
        
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