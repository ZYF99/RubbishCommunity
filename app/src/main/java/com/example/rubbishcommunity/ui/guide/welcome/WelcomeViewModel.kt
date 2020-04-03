package com.example.rubbishcommunity.ui.guide.welcome

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.RubbishService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.persistence.getClassificationList
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import com.hankcs.hanlp.HanLP
import io.reactivex.Single
import org.kodein.di.generic.instance


class WelcomeViewModel(application: Application) : BaseViewModel(application) {
	private val rubbishService by instance<RubbishService>()
	val searchWord = MutableLiveData("纸巾")
	val searchResultList = MutableLiveData<MutableList<SearchKeyConclusion>>()
	
	
	fun search() {
		if ((searchWord.value ?: "").isNotEmpty()) {
			Single.fromCallable {
				//HanLp做一次预操作
				HanLP.extractSummary(searchWord.value, 1)[0]
			}.flatMap {
				rubbishService.searchClassification(it)
			}.switchThread()
				.doOnSuccess { result ->
					result.data.forEach { searchKeyConclusion ->
						searchKeyConclusion.category = getClassificationList().filter { category ->
							category.id == searchKeyConclusion.sortId
						}[0]
					}
					searchResultList.value = result.data
				}
				.compose(dealErrorCode())
				.compose(catchApiError())
				.bindLife()
		}
	}


/*    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs
                .doOnSubscribe { isLoading.postValue(true) }
                .doOnSuccess { isLoading.postValue(false) }
                .doOnError { isLoading.postValue(false) }
        }
    }*/
	
	
}