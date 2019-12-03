package com.example.rubbishcommunity.ui.home.homepage

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.JuheService
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.model.api.News
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class HomePageViewModel(application: Application) : BaseViewModel(application) {
	
	
	private val rubbishService by instance<RubbishService>()
	
	private val juheService by instance<JuheService>()
	
	
	val isRefreshing = MutableLiveData<Boolean>()
	
	val newsList = MutableLiveData<MutableList<News>>(mutableListOf())
	val photographyList = MutableLiveData<MutableList<Photography>>(mutableListOf())
	
	fun getNews() {
		juheService.getNews()
			.switchThread()
			.doOnSuccess {
				newsList.value = it.result.data
			}.compose(dealErrorCode())
			.compose(dealError())
			.compose(dealRefresh())
			.bindLife()
	}
	
	fun getPhotography() {
		mockData()
/*		juheService.getPhotography()
			.switchThread()
			.doOnSuccess {
				photographyList.value = it
			}
			.compose(dealError())
			.compose(dealRefresh())
			.bindLife()*/
	}
	
	private fun mockData() {
		photographyList.value = mutableListOf(
			Photography(
				"",
				"",
				"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573557155327&di=44f5ec8ef0f6fcbbf28131b3f9af5df5&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2F20171020%2F68fc-fymzzpv8123943.jpg"
			),
			Photography(
				"",
				"",
				"http://img1.imgtn.bdimg.com/it/u=3053304738,3863903291&fm=26&gp=0.jpg"
			),
			Photography(
				"",
				"",
				"http://img3.imgtn.bdimg.com/it/u=3774166848,2531253577&fm=26&gp=0.jpg"
			)
		)
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
	
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs
				.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
	
	
}