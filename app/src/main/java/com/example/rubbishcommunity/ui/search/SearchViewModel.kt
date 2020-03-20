package com.example.rubbishcommunity.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.persistence.getClassificationMap
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
					searchKeyConclusion.category = getClassificationMap().filter { category ->
						category.id == searchKeyConclusion.sortId
					}[0]
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