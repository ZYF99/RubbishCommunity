package com.example.rubbishcommunity.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.persistence.getClassificationList
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import com.hankcs.hanlp.HanLP
import io.reactivex.Single
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class SearchViewModel(application: Application) : BaseViewModel(application) {
	
	private val rubbishService by instance<RubbishService>()
	
	val isLoading = MutableLiveData(false)
	
	val searchList = MutableLiveData<MutableList<SearchKeyConclusion>>()
	
	val searchKey = MutableLiveData("")
	
	//位置
	val location = MutableLiveData<BDLocation>()
	
	val shouldShowInput = MutableLiveData<Boolean>()
	
	
	fun search(str: String): Single<ResultModel<MutableList<SearchKeyConclusion>>> {
		return rubbishService.searchClassification(str)
			.switchThread()
			.doOnSuccess {
				it.data.map { searchKeyConclusion ->
					//将服务器返回的垃圾sortId拿到本地的HList中查询对应的垃圾分类信息
					//将查到的垃圾分类对象赋值到当前searchKeyConclusion对象中
					searchKeyConclusion.category = getClassificationList().first { category ->
						category.id == searchKeyConclusion.sortId
					}
				}.toMutableList()
			}
			.compose(dealErrorCode())
			.compose(catchApiError())
			.compose(dealRefresh())
	}
	
	fun analysisAndSearch() {
		Single.just(searchKey.value).flatMap {
			Single.just(HanLP.extractSummary(it, 10))
		}.switchThread()
			.flatMap {
				search(it[0])
			}.doOnSuccess {
				searchList.postValue(it.data)
				shouldShowInput.postValue(false)
			}.compose(dealRefresh())
			.bindLife()
	}
	
	fun testSearchKeyWord() {
		search("水瓶")
			.doOnSuccess {
				searchList.postValue(it.data) //将查询的列表结果赋值给当前界面的列表
				shouldShowInput.postValue(false) //控制软键盘的状态变量
			}.compose(dealRefresh())
			.bindLife()
	}
	
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs
				.doOnSubscribe { isLoading.postValue(true) }
				.doOnSuccess { isLoading.postValue(false) }
				.doOnError { isLoading.postValue(false) }
		}
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
	
	
}