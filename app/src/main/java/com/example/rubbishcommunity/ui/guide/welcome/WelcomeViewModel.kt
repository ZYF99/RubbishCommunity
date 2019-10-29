package com.example.rubbishcommunity.ui.guide.welcome

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class WelcomeViewModel(application: Application) : BaseViewModel(application) {
	private val apiService by instance<ApiService>()
	val searchWord = MutableLiveData<String>()
	
	val photograpgyList = MutableLiveData<List<Photography>>()
	
	fun search(str: String): Single<ResultModel<List<SearchKeyConclusion>>> {
		return apiService.searchClassification(str)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealErrorCode())
			.compose(dealError())
	}
	
	fun getPhotographyList(): Single<ResultModel<List<Photography>>> {
		return apiService.getPhotographyList()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(dealErrorCode())
			.compose(dealError())
			.doOnSubscribe {
				photograpgyList.postValue(
					listOf(
						Photography("没水的水瓶", "没水的水瓶是干垃圾哦～～", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571037072971&di=3e189cf0d3f891d9f316881cff126ad0&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201902%2F03%2F20190203191157_gnqls.png"),
						Photography("没水的水瓶", "没水的水瓶是干垃圾哦～～", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571037072971&di=bbe7f6586bee4325c473aff27514ad50&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201702%2F05%2F20170205181400_wrdxe.thumb.700_0.jpeg"),
						Photography("没水的水瓶", "没水的水瓶是干垃圾哦～～", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571037072970&di=6075bac836a4082e25c77c628609b246&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fed053e4f5972419836f26380f2843fe62607eeb51a6b3-MrnaLA_fw658"),
						Photography("没水的水瓶", "没水的水瓶是干垃圾哦～～", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571037072970&di=e8f5c3135792f0607d8f4c7ad42f1e4d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201702%2F05%2F20170205213208_TtEZh.thumb.700_0.jpeg")
					
					)
				)
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