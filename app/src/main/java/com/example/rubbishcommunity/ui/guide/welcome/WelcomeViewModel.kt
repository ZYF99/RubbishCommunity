package com.example.rubbishcommunity.ui.guide.welcome

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.search.Category
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.BaseViewModel
import com.hankcs.hanlp.HanLP
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class WelcomeViewModel(application: Application) : BaseViewModel(application) {
	private val apiService by instance<ApiService>()
	val searchWord = MutableLiveData<String>()
	val searchResultList = MutableLiveData<MutableList<SearchKeyConclusion>>()
	
	
	fun search() {
		if ((searchWord.value?:"").isNotEmpty()) {
			Single.fromCallable {
				HanLP.extractSummary(searchWord.value, 1)[0]
			}.flatMap {
				apiService.searchClassification(it)
			}.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSuccess { result ->
					result.data.forEach { searchConclusion ->
						searchConclusion.category =
							MyApplication.instance.classificationMap[searchConclusion.sortId]
								?: Category.getNull()
					}
					searchResultList.value = result.data
				}
				.compose(dealErrorCode())
				.compose(dealError())
				.bindLife()
		}
	}


/*    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs
                .doOnSubscribe { isRefreshing.postValue(true) }
                .doOnSuccess { isRefreshing.postValue(false) }
                .doOnError { isRefreshing.postValue(false) }
        }
    }*/
	
	
}