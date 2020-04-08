package com.example.rubbishcommunity.ui.home.homepage

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.JuheService
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.model.api.News
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import io.reactivex.SingleTransformer
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
			}.catchApiError()
			.compose(dealRefresh())
			.bindLife()
	}
	
	fun getPhotography() {
		mockData()
		juheService.getPhotography()
			.switchThread()
			.doOnSuccess {
				photographyList.value = it
			}
			.catchApiError()
			.compose(dealRefresh())
			.bindLife()
	}
	
	private fun mockData() {
		val src =
			"https://img.iplaysoft.com/wp-content/uploads/2017/ios11_wallpapers/ios11_earth.jpg!0x0.webp"
		photographyList.value = mutableListOf(
			Photography(
				"",
				"",
				src
			),
			Photography(
				"",
				"",
				src
			),
			Photography(
				"",
				"",
				src
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
			.switchThread()
			.compose(dealErrorCode())
			.compose(catchApiError())
	}
	
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnSubscribe { isRefreshing.postValue(true) }
				.doFinally { isRefreshing.postValue(false) }
		}
	}
	
	
}