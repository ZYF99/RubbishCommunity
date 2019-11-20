package com.example.rubbishcommunity.ui.home.homepage

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.JuheService
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.News
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class HomePageViewModel(application: Application) : BaseViewModel(application) {
	
	
	private val rubbishService by instance<RubbishService>()
	private val juheService by instance<JuheService>()
	
	private val hotWordList = MutableLiveData<MutableList<String>>()
	private val searchWord = MutableLiveData<String>()
	
	val isRefreshing = MutableLiveData<Boolean>()
	
	val newsList = MutableLiveData<MutableList<News>>()
	
	
	fun init() {
		hotWordList.value = mutableListOf()
		searchWord.postValue("")
		getHotWordList()
	}
	
	fun getNews(){
		juheService.getNews()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealErrorCode())
			.compose(dealError())
			.compose(dealRefresh())
			.doOnSuccess {
				newsList.value = it.result.data
			}
			.bindLife()
		
	}
	
/*	fun protobufTest(){
		apiService.protobufGetTest()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			//.compose(dealErrorCode())
			.compose(dealError())
			.compose(dealRefresh())
			.doOnSuccess {
				MyApplication.showSuccess(it.toString())
			}
			.bindLife()
		
	}*/
	
	
	fun search(str: String): Single<ResultModel<MutableList<SearchKeyConclusion>>> {
		return rubbishService.searchClassification(str)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealErrorCode())
			.compose(dealError())
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


    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs
                .doOnSubscribe { isRefreshing.postValue(true) }
                .doOnSuccess { isRefreshing.postValue(false) }
                .doOnError { isRefreshing.postValue(false) }
        }
    }
	
	
}